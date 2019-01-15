package mu.snuhacks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mu.snuhacks.Adapters.AttendanceAdapter;

public class CreditAttendanceF extends Fragment {
    private final String TAG = CreditAttendanceF.class.getSimpleName();

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
    private boolean isConnected = true;
    private ProgressBar mprogress;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreateCalled");
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

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle savedInstanceState){
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
                FetchAttendanceTask fetchAttendanceTask3 = new FetchAttendanceTask();
                fetchAttendanceTask3.execute(new String[]{netId,password});
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

    private class FetchAttendanceTask extends AsyncTask<String,Void,AttendanceF.AttendanceResponse> {
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
        protected AttendanceF.AttendanceResponse doInBackground(String... credentials) {
            if(isConnected) {
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
                        return new AttendanceF.AttendanceResponse(null,"Invalid Credentials");
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
                            Elements panelElements = checkAttendanceDoc.getElementsByClass("panel panel-primary");
                            Elements trElements = panelElements.select("tr");
                            for (int i = 0; i < trElements.size() - 1; i++) {
                                Element trElement = trElements.get(i + 1);
                                Elements tdElements = trElement.select("td");
                                attendanceData.add(new AttendanceDataCC(tdElements.get(0).text().substring(tdElements.get(0).text().indexOf('-')+1,tdElements.get(0).text().length()),
                                        tdElements.get(0).text().substring(0,tdElements.get(0).text().indexOf('-')),
                                        (Double.parseDouble(tdElements.get(1).text()) + Double.parseDouble(tdElements.get(2).text()) + Double.parseDouble(tdElements.get(3).text())) + "",
                                        tdElements.get(7).text(),
                                        tdElements.get(8).text(),
                                        tdElements.get(9).text(),
                                        tdElements.get(14).text()
                                ));
                            }
                        } catch (Exception exception) {
                            Log.d(TAG, "Exception:- " + exception.getMessage());
                        }
                    }
                } catch(Exception exception){
                    Log.d(TAG,"Exception:- " + exception.getMessage());
                }
                return new AttendanceF.AttendanceResponse(attendanceData,"");
            }
            return new AttendanceF.AttendanceResponse(null,"Not connected to Student Wifi");
        }

        @Override
        public void onPostExecute(AttendanceF.AttendanceResponse response){
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
