package mu.snuhacks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

    private ArrayList<Object> attendanceData;
    private String netId;
    private String password;
    private Depth depth;
    private boolean isConnected;
    private ProgressBar mprogress;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreateCalled");
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
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
            //    swipeRefreshLayout.setRefreshing(true);
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
       // mprogress = view.findViewById(R.id.ProgressBar);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
        attendanceView.setLayoutManager(manager);
        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG,"onRefresh() called");
              //  swipeRefreshLayout.setRefreshing(true);
                Log.d("isconnected", String.valueOf(isConnected));
                FetchAttendanceTask fetchAttendanceTask3 = new FetchAttendanceTask();
                fetchAttendanceTask3.execute(new String[]{netId,password});
                checkAndModifyWifiState();
              //  checkAndModifyWifiState();
            /*    if(isConnected == true){
                    if(emptyTextView.getVisibility()==View.VISIBLE)
                        emptyTextView.setVisibility(View.GONE);
                    fetchAttendanceTask3 = new FetchAttendanceTask();
                    fetchAttendanceTask3.execute(new String[]{netId,password});
                }
                else {emptyTextView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                } */
            }
        };
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        String attendace = sharedPreferences.getString("attendance","");
        if(attendace.length() == 0){
       //     emptyTextView.setVisibility(View.VISIBLE);
         //   attendanceView.setVisibility(View.GONE);
            Log.d(TAG,"Executing hereaa");

        } else{
            try {
                attendanceData = (ArrayList<Object>) ObjectSerializer.deserialize(attendace);
            } catch(Exception exception){
                Log.d(TAG,"Exception:- " + exception.getMessage());
            }
            emptyTextView.setVisibility(View.GONE);
            Log.d(TAG,"Executing here2aaws");

            attendanceView.setVisibility(View.VISIBLE);
            Log.d(TAG,"Executing here2aaewew");

            adapter = new AttendanceAdapter(attendanceData, getActivity().getApplicationContext());
            Log.d(TAG,"Executing here2aaewew");

            attendanceView.setAdapter(adapter);
            Log.d(TAG,"Executing here2aaqeqe");

        }
        return view;
    }

    private void checkAndModifyWifiState(){
        Log.d(TAG,"checkModifyState() executing");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        Log.d("Wifi name: ",mWifiManager.getConnectionInfo().getSSID());
        if(mWifiManager != null && !isConnected){
            Log.d("Wifi name: ",mWifiManager.getConnectionInfo().getSSID());
            if(!mWifiManager.isWifiEnabled()){
                Log.d(TAG,"Wifi enabled");
                builder.setTitle("Switch on Wifi?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        mWifiManager.disconnect();
                        Log.d(TAG,"Trying");
                        mWifiManager.setWifiEnabled(true);
                        if("".equals(mWifiManager.getConnectionInfo().getSSID())){
                            isConnected = true;
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onResume();
                            }
                        }, 7000);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"Wifi off");
                        isConnected = false;
                        mWifiManager = null;
                        Log.d(TAG,"mWifiManager null");
                        dialog.dismiss();
                    }
                });
                AlertDialog deleteDialog = builder.create();
                deleteDialog.show();
            }
            if(mWifiManager.getConnectionInfo().getSSID().equals("\"Student\"")||mWifiManager.getConnectionInfo().getSSID().equals("<unknown ssid>")){
                Log.d("Wifi name here: ",mWifiManager.getConnectionInfo().getSSID());
                isConnected = true;
            } else {
                List<WifiConfiguration> wifiConfigurations = mWifiManager.getConfiguredNetworks();
                for (final WifiConfiguration configuration : wifiConfigurations) {
                    if (configuration.SSID.equals("Student")) {
                        builder.setTitle("Connect to Student Wifi?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int which) {
                                mWifiManager.disconnect();
                                mWifiManager.enableNetwork(configuration.networkId,true);
                                Log.d(TAG,"Trying");
                                isConnected = mWifiManager.reconnect();
                                dialog.dismiss();
                            }


                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG,"Deletion cancelled");
                                isConnected = false;
                                Log.d(TAG,"mWifiManager null");
                                dialog.dismiss();
                            }
                        });
                        mWifiManager.disconnect();
                        mWifiManager.enableNetwork(configuration.networkId,true);
                        Log.d(TAG,"Trying");
                        isConnected = mWifiManager.reconnect();
                        AlertDialog deleteDialog = builder.create();
                        deleteDialog.show();

                    }
                }
            }
        } else{
            isConnected = false;
            Log.d(TAG,"mWifiManager null");
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
            if(emptyTextView.getVisibility()==View.VISIBLE){
                emptyTextView.setVisibility(View.GONE);
            }
        }


        @Override
        protected AttendanceResponse doInBackground(String... credentials) {
                Log.d(TAG, "doInBackground() executing");
                ArrayList<Object> attendanceData = new ArrayList<Object>();
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
                                attendanceData.add(new AttendanceData(tdElements.get(1).text(), tdElements.get(0).text(), tdElements.get(2).text(),tdElements.get(3).text(),tdElements.get(4).text(),tdElements.get(5).text(), tdElements.get(6).text()));

                            }
                        } catch (Exception exception) {
                            Log.d(TAG, "Exception2:- " + exception.getMessage());
                        }
                    }
                } catch(Exception exception){
                    Log.d(TAG,"Exception1:- " + exception.getMessage());
                    if(exception.getMessage().equals("Failed to connect to markattendance.webapps.snu.edu.in/10.2.34.4:443")){
                        return new AttendanceResponse(null,"Not connected to Student Wifi");

                    }
                }

              return new AttendanceResponse(attendanceData,"");
        }

        @Override
        public void onPostExecute(AttendanceResponse response){
            Log.d(TAG,"onPostExecute() executing");
            if(swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            if(response.getErrorMessage().length() != 0){
                Log.d("response.erorr",response.getErrorMessage());
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
                Log.d(TAG,"onPostExecute() executing..");
                if(response.getAttendanceData().size() > 0) {
                    try {
                        prefs = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
                        editor = prefs.edit();
                        editor.putString("attendance", ObjectSerializer.serialize(response.getAttendanceData()));
                        editor.apply();
                    } catch (IOException exception) {
                        Log.d(TAG, "IOException:- " + exception.getMessage());
                    }
                    emptyTextView.setVisibility(View.GONE);
                    attendanceView.setVisibility(View.VISIBLE);
                    attendanceData.clear();
                    attendanceData.addAll(response.getAttendanceData());

                    if (adapter != null) {
                        adapter.setAttendanceData(attendanceData);
                    } else {
                        adapter = new AttendanceAdapter(attendanceData,getActivity().getApplicationContext());
                        attendanceView.setAdapter(adapter);
                    }
                } else{
                    attendanceView.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
