package mu.snuhacks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.florent37.depth.Depth;
import com.github.florent37.depth.DepthProvider;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mu.snuhacks.Adapters.AttendanceAdapterCC;

public class CreditAttendanceF extends Fragment {
    private final String TAG = CreditAttendanceF.class.getSimpleName();

    private SharedPreferences sharedPreferences;

    private ConstraintLayout constraintLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    String totalAttendance;

    private RecyclerView attendanceView;
    private TextView emptyTextView;
    private TextView totalAttendanceTextView;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    private AttendanceAdapterCC adapter;

    private ArrayList<Object> attendanceData;
    private String netId;
    private String password;
    private Depth depth;
    private boolean isConnected;
    private ProgressBar mprogress;
    private View lineview;
    Double course_credit;
    Double total_credit =0.0;
    Double total = 0.0;
    Double displayed = 0.0;
    WifiManager mWifiManager;
    private InterstitialAd mInterstitialAd;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreateCalled");
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-2461190858191596/4980119936");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mWifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        netId = sharedPreferences.getString("username","");
        password = sharedPreferences.getString("password","");
        if(netId.length() == 0 || password.length() == 0){
            Intent loginIntent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
        attendanceData = new ArrayList<Object>();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"onResume() called");
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefreshListener.onRefresh();
            }
        });
    }
    private void checkAndModifyWifiState(){
        Log.d(TAG,"checkModifyState() executing");
        mWifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.d("Wifi name: ",mWifiManager.getConnectionInfo().getSSID());
        Log.d(TAG,mWifiManager.toString());
        Log.d(TAG, String.valueOf(isConnected));
        if(mWifiManager != null && !isConnected){
            Log.d("Wifi name: ",mWifiManager.getConnectionInfo().getSSID());
            if(!mWifiManager.isWifiEnabled()){
                Log.d(TAG,"Wifi enabled");
                mWifiManager.setWifiEnabled(true);
            }
            if(mWifiManager.getConnectionInfo().getSSID().equals("\"Student\"")){
                Log.d("Wifi name here: ",mWifiManager.getConnectionInfo().getSSID());
                isConnected = true;
            } else {
                List<WifiConfiguration> wifiConfigurations = mWifiManager.getConfiguredNetworks();
                for (WifiConfiguration configuration : wifiConfigurations) {
                    if (configuration.SSID.equals("Student")) {
                        mWifiManager.disconnect();
                        mWifiManager.enableNetwork(configuration.networkId,true);
                        Log.d(TAG,"Trying");
                        isConnected = mWifiManager.reconnect();
                    }
                }
            }
        } else{
            if(isConnected == true)
                isConnected = true;
            else{
            isConnected = false;
            Log.d(TAG,"mWifiManager null");}
        }
    }
    private static BigDecimal truncateDecimal(double x, int numberofDecimals)
    {
        if ( x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    }
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle savedInstanceState){
        this.depth = DepthProvider.getDepth(parent);
        Log.d("tag","creating AttendanceF");
        View view = depth.setupFragment(10f, 10f, layoutInflater.inflate(R.layout.attendance_fragment_cc, parent, false));
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.parent_layout_here2);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh2);
        attendanceView = (RecyclerView) view.findViewById(R.id.attendance_recycler_view2);

        emptyTextView = (TextView) view.findViewById(R.id.empty_text_view2);
        lineview = view.findViewById(R.id.line);
        totalAttendanceTextView = view.findViewById(R.id.totalAttendance);
        // mprogress = view.findViewById(R.id.ProgressBar);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
        attendanceView.setLayoutManager(manager);
        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                FetchCCAttendanceTask fetchAttendanceTask3 = new FetchCCAttendanceTask();

                Log.d(TAG,"onRefresh() called");
                checkAndModifyWifiState();
                if(isConnected == true){
                    if(emptyTextView.getVisibility()==View.VISIBLE)
                        emptyTextView.setVisibility(View.GONE);
                    fetchAttendanceTask3.execute(new String[]{netId,password});
                }
                else {emptyTextView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        };
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        String attendace = sharedPreferences.getString("attendance","");
        if(attendace.length() == 0){
            emptyTextView.setVisibility(View.VISIBLE);
            attendanceView.setVisibility(View.GONE);
            Log.d(TAG,"Executing hereaa");

        } else{
            try {
                attendanceData = (ArrayList<Object>) ObjectSerializer.deserialize(attendace);
            } catch(Exception exception){
                Log.d(TAG,"Exception:- " + exception.getMessage());
            }
            emptyTextView.setVisibility(View.GONE);
            attendanceView.setVisibility(View.VISIBLE);
            adapter = new AttendanceAdapterCC(attendanceData, getActivity().getApplicationContext());
            attendanceView.setAdapter(adapter);

        }
        return view;
    }

    private class FetchCCAttendanceTask extends AsyncTask<String,Void,AttendanceResponse> {
        private final String TAG = FetchCCAttendanceTask.class.getSimpleName();

        private SharedPreferences prefs;
        private SharedPreferences.Editor editor;

        @Override
        public void onPreExecute(){
            Log.d(TAG,"onPreExecute() executing");
            if(!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }
        }


        @Override
        protected AttendanceResponse doInBackground(String... credentials) {
                 total_credit =0.0;
                 total = 0.0;
                 displayed = 0.0;

                Log.d(TAG, "doInBackground() executing");
                ArrayList<Object> ccattendanceData = new ArrayList<Object>();
                try{
                    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }};
                    try {
                        SSLContext sc = SSLContext.getInstance("SSL");
                        sc.init(null, trustAllCerts, new java.security.SecureRandom());
                        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Log.d(TAG,"Executing here wrwrw");
                    Connection.Response login = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/login/loginAuthSubmit")
                            .data("login_user_name", credentials[0])
                            .data("login_password", credentials[1])
                            .userAgent("Mozilla")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .method(Connection.Method.POST)
                            .execute();
                    Log.d(TAG,"Executing here2");

                    Document loginDoc = login.parse();
                    Elements errorElements = loginDoc.getElementsByClass("alert-warning");
                    if(errorElements.size() != 0){
                        return new AttendanceResponse(null,"Invalid Credentials");
                    } else {
                        Log.d(TAG,"Executing here3");

                        Connection.Response checkAttendance = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/index/crs_wise_att_ch")
                                .userAgent("Mozilla")
                                .header("X-Requested-With", "XMLHttpRequest")
                                .method(Connection.Method.POST)
                                .cookies(login.cookies())
                                .execute();
                        Document checkAttendanceDoc = checkAttendance.parse();
                        try {
                            Log.d(TAG,"yaha bhi aa gaya");
                            Elements panelElements = checkAttendanceDoc.getElementsByTag("tbody");
                            Elements trElements = panelElements.select("tr");
                            Log.d(TAG+"@@", String.valueOf(trElements.size()));
                            attendanceData.clear();
                            for (int i = 0; i <= trElements.size() - 1; i++) {
                                Element trElement = trElements.get(i);
                                Elements tdElements = trElement.select("td");
                                Log.d(TAG,tdElements.toString());

                                course_credit = (Double.parseDouble(tdElements.get(1).text()) + Double.parseDouble(tdElements.get(2).text()) + Double.parseDouble(tdElements.get(3).text()));
                                total_credit = total_credit + course_credit;
                                Log.d("total",total_credit.toString());

                                Double number1 = Double.parseDouble(tdElements.get(14).text().replace("%",""));
                                total = total + (course_credit* number1);
                                Log.d("total",total.toString());

                                attendanceData.add(new AttendanceDataCC(tdElements.get(0).text().substring(tdElements.get(0).text().indexOf('-')+1,tdElements.get(0).text().length()),
                                        tdElements.get(0).text().substring(0,tdElements.get(0).text().indexOf('-')),
                                        (Double.parseDouble(tdElements.get(1).text()) + Double.parseDouble(tdElements.get(2).text()) + Double.parseDouble(tdElements.get(3).text())) + "",
                                        tdElements.get(7).text(),
                                        tdElements.get(8).text(),
                                        tdElements.get(9).text(),
                                        tdElements.get(14).text().replace("%","")
                                ));
                                Log.e("data",attendanceData.toString());

                            }
                        } catch (Exception exception) {
                            Log.d(TAG, "Exception:- " + exception.getMessage());
                        }
                        return new AttendanceResponse(attendanceData,"");
                    }
                } catch(Exception exception){
                    Log.d(TAG,"Exception:- " + exception.getMessage());

                }

            return new AttendanceResponse(null,"Not connected to Student Wifi");
        }

        @Override
        public void onPostExecute(final AttendanceResponse response){
            Random rand = new Random();
            int x = rand.nextInt(5);
            if (mInterstitialAd != null && mInterstitialAd.isLoaded()&&x==0) {
                mInterstitialAd.show();
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        Log.d(TAG,"onPostExecute() executing");
                        //Log.d(TAG, String.valueOf(response.getAttendanceData().size()));
                        try{
                            emptyTextView.setVisibility(View.GONE);
                            Log.d(TAG,emptyTextView.toString());}
                        catch (Exception e)
                        {
                            Log.d(TAG, String.valueOf(e.getStackTrace()));
                        }
                        if(swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        if(response.getErrorMessage().length() != 0){
                            Snackbar.make(constraintLayout,response.getErrorMessage(),Snackbar.LENGTH_SHORT);
                            try{
                                emptyTextView.setVisibility(View.VISIBLE);
                            }
                            catch (Exception e){
                                Log.d(TAG,e.getMessage());
                            }
                            if(response.getErrorMessage().equals("Invalid Credentials")){
                                Intent loginIntent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(loginIntent);
                            }
                        } else {
                            Log.d("size",String.valueOf(response.getAttendanceData().size()));
                            if(response.getAttendanceData().size() > 0) {
                                try {
                                    prefs = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
                                    editor = prefs.edit();
                                    displayed = total/total_credit;
                                    //to truncate to 2 decimal places
                                    BigDecimal d = truncateDecimal(displayed,2);
                                    Log.d("displated",d.toString());
                                    if(displayed<75.0)
                                        totalAttendance =  "<font color=#ff0000>" + d.toString()+"%</font>";
                                    else
                                        totalAttendance =  "<font color=#13c000>" + d.toString()+"%</font>";
                                    String textviewText = "<font color=#FFFFFF> Total Attendance -  </font>" +  totalAttendance;
                                    lineview.setVisibility(View.VISIBLE);
                                    totalAttendanceTextView.setText(Html.fromHtml(textviewText));
                                    editor.putString("total",totalAttendance);
                                    editor.apply();
                                    // editor.putString("attendance", ObjectSerializer.serialize(response.getAttendanceData()));


                                } catch (Exception exception) {
                                    Log.d(TAG, "IOException:- " + exception.getMessage());
                                }
                                emptyTextView.setVisibility(View.GONE);
                                attendanceView.setVisibility(View.VISIBLE);
                                Log.e(TAG,attendanceData.toString());
                                if (adapter != null) {
                                    Log.d("ad",attendanceData.toString());
                                    adapter.setAttendanceData(response.getAttendanceData());
                                } else {

                                    Log.d("aad",attendanceData.toString());
                                    adapter = new AttendanceAdapterCC(response.getAttendanceData(),getActivity().getApplicationContext());
                                    attendanceView.setAdapter(adapter);
                                }
                            } else{
                                attendanceView.setVisibility(View.GONE);
                                // emptyTextView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            } else {
               // Toast.makeText(getContext(), "Ad did not load", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"onPostExecute() executing");
                //Log.d(TAG, String.valueOf(response.getAttendanceData().size()));
                try{
                    emptyTextView.setVisibility(View.GONE);
                    Log.d(TAG,emptyTextView.toString());}
                catch (Exception e)
                {
                    Log.d(TAG, String.valueOf(e.getStackTrace()));
                }
                if(swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if(response.getErrorMessage().length() != 0){
                    Snackbar.make(constraintLayout,response.getErrorMessage(),Snackbar.LENGTH_SHORT);
                    try{
                        emptyTextView.setVisibility(View.VISIBLE);
                    }
                    catch (Exception e){
                        Log.d(TAG,e.getMessage());
                    }
                    if(response.getErrorMessage().equals("Invalid Credentials")){
                        Intent loginIntent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                    }
                } else {
                    Log.d("size",String.valueOf(response.getAttendanceData().size()));
                    if(response.getAttendanceData().size() > 0) {
                        try {
                            prefs = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
                            editor = prefs.edit();
                            displayed = total/total_credit;
                            //to truncate to 2 decimal places
                            BigDecimal d = truncateDecimal(displayed,2);
                            Log.d("displated",d.toString());
                            if(displayed<75.0)
                                totalAttendance =  "<font color=#ff0000>" + d.toString()+"%</font>";
                            else
                                totalAttendance =  "<font color=#13c000>" + d.toString()+"%</font>";
                            String textviewText = "<font color=#FFFFFF> Total Attendance -  </font>" +  totalAttendance;
                            lineview.setVisibility(View.VISIBLE);
                            totalAttendanceTextView.setText(Html.fromHtml(textviewText));
                            editor.putString("total",totalAttendance);
                            editor.apply();
                            // editor.putString("attendance", ObjectSerializer.serialize(response.getAttendanceData()));


                        } catch (Exception exception) {
                            Log.d(TAG, "IOException:- " + exception.getMessage());
                        }
                        emptyTextView.setVisibility(View.GONE);
                        attendanceView.setVisibility(View.VISIBLE);
                        Log.e(TAG,attendanceData.toString());
                        if (adapter != null) {
                            Log.d("ad",attendanceData.toString());
                            adapter.setAttendanceData(response.getAttendanceData());
                        } else {
                            Log.d("aad",attendanceData.toString());
                            adapter = new AttendanceAdapterCC(response.getAttendanceData(),getActivity().getApplicationContext());
                            attendanceView.setAdapter(adapter);
                        }
                    } else{
                        attendanceView.setVisibility(View.GONE);
                        // emptyTextView.setVisibility(View.VISIBLE);
                    }
                }

            }
        }



    }

}
