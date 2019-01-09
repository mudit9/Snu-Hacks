package mu.snuhacks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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

import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mu.snuhacks.Adapters.AttendanceAdapter;

public class MarkAttendanceF extends Fragment {
    private final String TAG = AttendanceF.class.getSimpleName();


    private WifiManager mWifiManager;
    private SharedPreferences sharedPreferences;
    private String htmlContentInStringFormat;

    private ConstraintLayout constraintLayout1;
    private SwipeRefreshLayout swipeRefreshLayout1;
    private RecyclerView attendanceView1;
    private TextView emptyTextView1;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener1;
    private AttendanceAdapter adapter;

    private ArrayList<AttendanceData> attendanceData;
    private String netId;
    private boolean flag;
    private String password;
    private View view1;
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

    }

    public static Fragment newInstance() {
        final Fragment fragment1 = new MarkAttendanceF();
        return fragment1;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle savedInstanceState){
        this.depth = DepthProvider.getDepth(parent);
        View view = depth.setupFragment(10f, 10f, layoutInflater.inflate(R.layout.mark_attendance_f, parent, false));
        return view;
    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        depth.onFragmentReady(this);

        view.findViewById(R.id.buttonView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AttendanceActivity) getActivity()).changeFragment(MarkAttendanceF.this);
            }
        });

        constraintLayout1 = (ConstraintLayout) view.findViewById(R.id.parent_layout1);
        swipeRefreshLayout1 = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh1);
        attendanceView1 = (RecyclerView) view.findViewById(R.id.attendance_recycler_view1);
        emptyTextView1 = (TextView) view.findViewById(R.id.empty_text_view1);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
        attendanceView1.setLayoutManager(manager);
        MarkAttendanceF.FetchAttendanceTask2 fetchAttendanceTask4 = new MarkAttendanceF.FetchAttendanceTask2();
        fetchAttendanceTask4.execute(netId,password);


        //  ((AttendanceActivity) getActivity()).changeFragment(MarkAttendanceF.this);


        onRefreshListener1 = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG,"onRefresh() called");
            }
        };
        String attendace = sharedPreferences.getString("attendance","");
        if(attendace.length() == 0){
            emptyTextView1.setVisibility(View.VISIBLE);
            attendanceView1.setVisibility(View.GONE);
        } else{
            try {
                attendanceData = (ArrayList<AttendanceData>) ObjectSerializer.deserialize(attendace);
            } catch(Exception exception){
                Log.d(TAG,"Exception:- " + exception.getMessage());
            }
            emptyTextView1.setVisibility(View.GONE);
            attendanceView1.setVisibility(View.VISIBLE);
            adapter = new AttendanceAdapter(attendanceData);
            attendanceView1.setAdapter(adapter);
        }
       /* view.findViewById(R.id.open_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ((AttendanceActivity) getActivity()).openResetFragment(Fragment1.this);
            }
        }); */
    }

    private class FetchAttendanceTask2 extends AsyncTask<String,Void,AttendanceF.AttendanceResponse> {
        private final String TAG = MarkAttendanceF.FetchAttendanceTask2.class.getSimpleName();

        @Override
        public void onPreExecute(){
            Log.d(TAG,"onPreExecute() executing");
            Log.d("tag","adada");
            if(!swipeRefreshLayout1.isRefreshing()) {
                swipeRefreshLayout1.setRefreshing(true);
            }
        }


        @Override
        protected AttendanceF.AttendanceResponse doInBackground(String... credentials) {
                Log.d(TAG, "doInBackground() executing");
                //ArrayList<AttendanceData> attendanceData = new ArrayList<AttendanceData>();
                try {
                    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    } };

                    // Install the all-trusting trust manager
                    try {
                        SSLContext sc = SSLContext.getInstance("SSL");
                        sc.init(null, trustAllCerts, new java.security.SecureRandom());
                        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    Connection.Response html = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/login/loginAuthSubmit")
                            .data("login_user_name", netId)
                            .data("login_password", password)
                            .userAgent("Mozilla")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .method(Connection.Method.POST)
                            .execute();

                    Connection.Response html2 = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/index/submit_attendance")
                            .userAgent("Mozilla")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .method(Connection.Method.POST)
                            .cookies(html.cookies())
                            .execute();

                    Document doc = html2.parse();
                    Element alert = doc.getElementsByClass("alert alert-warning alert-dismissible").first();
                    Element success = doc.getElementsByClass("alert alert-success alert-dismissible").first();
                    if(alert!=null)
                    {flag = true;
                        htmlContentInStringFormat ="<br> <br> <font color = #ff0000>" + alert.text() + "</font>";}
                    else if(success!=null) {
                        flag = false;
                        htmlContentInStringFormat = "<br> <br> <font color = #13c000>" + success.text() + "</font>";
                    }
                    else
                        htmlContentInStringFormat = "Failed.";
                }
                catch (IOException e) {
                    htmlContentInStringFormat = "Probably not connected to Student Wifi.";
                }

            Log.d("HtmL",htmlContentInStringFormat);
            return null;
        }

        @Override
        public void onPostExecute(AttendanceF.AttendanceResponse response){
            Log.d(TAG,"onPostExecute() executing");
            Log.d(TAG,"EEEExecuting");

        /*    if(swipeRefreshLayout.isRefreshing()) {
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

                }
            }
            */
            if(swipeRefreshLayout1.isRefreshing()) {
                swipeRefreshLayout1.setRefreshing(false);}
            emptyTextView1.setVisibility(View.VISIBLE);
            emptyTextView1.setText(htmlContentInStringFormat);
            if(flag == true)
            ((AttendanceActivity) getActivity()).openResetFragment(MarkAttendanceF.this);
            else
                ((AttendanceActivity) getActivity()).changeFragment(MarkAttendanceF.this);




            //    ((AttendanceActivity) getActivity()).changeFragment(MarkAttendanceF.this);

        }
    }




}