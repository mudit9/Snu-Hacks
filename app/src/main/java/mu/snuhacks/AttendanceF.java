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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.florent37.depth.Depth;
import com.github.florent37.depth.DepthProvider;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mu.snuhacks.Adapters.AttendanceAdapter;

public class AttendanceF extends Fragment {
    private final String TAG = AttendanceF.class.getSimpleName();

    private WifiManager mWifiManager;
    private SharedPreferences sharedPreferences;

    private ConstraintLayout constraintLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView attendanceView;
    private TextView emptyTextView;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    private AttendanceAdapter adapter;

    private ArrayList<AttendanceData> attendanceData;
    private String netId;
    private String password;
    private Depth depth;
    private boolean isConnected = true;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mWifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        netId = sharedPreferences.getString("username","");
        password = sharedPreferences.getString("password","");
        if(netId.length() == 0 || password.length() == 0){
            Intent loginIntent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
        attendanceData = new ArrayList<AttendanceData>();
    }

    public static Fragment newInstance() {
        final Fragment fragment1 = new AttendanceF();
        return fragment1;
    }

    @Override
    public void onResume(){
        super.onResume();
        checkAndModifyWifiState();
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefreshListener.onRefresh();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent,Bundle savedInstanceState){
        this.depth = DepthProvider.getDepth(parent);
        Log.d("tag","creating AttendanceF");
        View view = depth.setupFragment(10f, 10f, layoutInflater.inflate(R.layout.attendance_fragment, parent, false));
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.parent_layout_here);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh);
        attendanceView = (RecyclerView) view.findViewById(R.id.attendance_recycler_view);
        emptyTextView = (TextView) view.findViewById(R.id.empty_text_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
        attendanceView.setLayoutManager(manager);
        FetchAttendanceTask fetchAttendanceTask3 = new FetchAttendanceTask();
      //  fetchAttendanceTask3.execute(new String[]{netId,password});
        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG,"onRefresh() called");

            }
        };
        String attendace = sharedPreferences.getString("attendance","");
        if(attendace.length() == 0){
            emptyTextView.setVisibility(View.VISIBLE);
            attendanceView.setVisibility(View.GONE);
            Log.d(TAG,"Executing hereaa");

        } else{
            try {
                attendanceData = (ArrayList<AttendanceData>) ObjectSerializer.deserialize(attendace);
            } catch(Exception exception){
                Log.d(TAG,"Exception:- " + exception.getMessage());
            }
            emptyTextView.setVisibility(View.GONE);
            Log.d(TAG,"Executing here2aaws");

            attendanceView.setVisibility(View.VISIBLE);
            Log.d(TAG,"Executing here2aaewew");

            adapter = new AttendanceAdapter(attendanceData);
            Log.d(TAG,"Executing here2aaewew");

            attendanceView.setAdapter(adapter);
            Log.d(TAG,"Executing here2aaqeqe");

        }
        return view;
    }

    private void checkAndModifyWifiState(){
        if(mWifiManager != null && !isConnected){
            if(!mWifiManager.isWifiEnabled()){
                mWifiManager.setWifiEnabled(true);
            }
            if(mWifiManager.getConnectionInfo().getSSID().equals("Student")){
                isConnected = true;
            } else {
                List<WifiConfiguration> wifiConfigurations = mWifiManager.getConfiguredNetworks();
                for (WifiConfiguration configuration : wifiConfigurations) {
                    if (configuration.SSID.equals("Student")) {
                        mWifiManager.disconnect();
                        mWifiManager.enableNetwork(configuration.networkId,true);
                        isConnected = mWifiManager.reconnect();
                    }
                }
            }
        }
    }

    private class FetchAttendanceTask extends AsyncTask<String,Void,AttendanceResponse> {
        private final String TAG = FetchAttendanceTask.class.getSimpleName();

        private SharedPreferences prefs;
        private SharedPreferences.Editor editor;

        @Override
        public void onPreExecute(){
            Log.d(TAG,"onPreExecute() executing");
            Log.d(TAG, "aafafa");
            if(!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }
        }


        @Override
        protected AttendanceResponse doInBackground(String... credentials) {
            if(isConnected) {
                Log.d(TAG, "doInBackground() executing");
                ArrayList<AttendanceData> attendanceData = new ArrayList<AttendanceData>();
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
                        throw   new RuntimeException(e);
                    }
                    Log.d(TAG,"Executing here");
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

                        Connection.Response checkAttendance = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/index/summary")
                                .userAgent("Mozilla")
                                .header("X-Requested-With", "XMLHttpRequest")
                                .method(Connection.Method.POST)
                                .cookies(login.cookies())
                                .execute();
                        Document checkAttendanceDoc = checkAttendance.parse();
                        try {
                            Elements panelElements = checkAttendanceDoc.getElementsByClass("panel panel-primary");
                            Elements trElements = panelElements.select("tr");
                            for (int i = 0; i < trElements.size() - 1; i++) {
                                Element trElement = trElements.get(i + 1);
                                Elements tdElements = trElement.select("td");
                                attendanceData.add(new AttendanceData(tdElements.get(1).text(), tdElements.get(0).text(), "", tdElements.get(6).text(), ""));
                            }
                        } catch (Exception exception) {
                            Log.d(TAG, "Exception:- " + exception.getMessage());
                        }
                    }
                } catch(Exception exception){
                    Log.d(TAG,"Exception:- " + exception.getMessage());
                }
                return new AttendanceResponse(attendanceData,"");
            }
            return new AttendanceResponse(null,"Not connected to Student Wifi");
        }

        @Override
        public void onPostExecute(AttendanceResponse response){
            Log.d(TAG,"onPostExecute() executing");
            if(swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            if(response.getErrorMessage().length() != 0){
                Snackbar.make(constraintLayout,response.getErrorMessage(),Snackbar.LENGTH_SHORT);
                if(response.getErrorMessage().equals("Invalid Credentials")){
                    Intent loginIntent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            } else {
                if(response.getAttendanceData().size() > 0) {
                    try {
                        prefs = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
                        editor = prefs.edit();
                        editor.putString("attendance", ObjectSerializer.serialize(response.getAttendanceData()));
                        editor.apply();
                    } catch (IOException exception) {
                        Log.d(TAG, "Exception:- " + exception.getMessage());
                    }
                    emptyTextView.setVisibility(View.GONE);
                    attendanceView.setVisibility(View.VISIBLE);
                    attendanceData.clear();
                    attendanceData.addAll(response.getAttendanceData());
                    if (adapter != null) {
                        adapter.setAttendanceData(attendanceData);
                    } else {
                        adapter = new AttendanceAdapter(attendanceData);
                        attendanceView.setAdapter(adapter);
                    }
                } else{
                    attendanceView.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public static class AttendanceResponse{
        private ArrayList<AttendanceData> attendanceData;
        private String errorMessage;

        public AttendanceResponse(ArrayList<AttendanceData> attendanceData,String errorMessage){
            this.attendanceData = attendanceData;
            this.errorMessage = errorMessage;
        }

        public ArrayList<AttendanceData> getAttendanceData(){
            return attendanceData;
        }

        public String getErrorMessage(){
            return errorMessage;
        }
    }
}
