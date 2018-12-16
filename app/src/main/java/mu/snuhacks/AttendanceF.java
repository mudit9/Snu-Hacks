package mu.snuhacks;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class AttendanceF extends Fragment {
    private final String TAG = AttendanceF.class.getSimpleName();

    private WifiManager mWifiManager;
    private SharedPreferences sharedPreferences;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    private String netId;
    private String password;
    private boolean isConnected = false;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mWifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        netId = sharedPreferences.getString("username","");
        password = sharedPreferences.getString("password","");
        if(netId.length() == 0 || password.length() == 0){
            //Redirect to Login activity
        }
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
        View view = layoutInflater.inflate(R.layout.attendance_fragment,parent,false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh);
        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG,"onRefresh() called");
                FetchAttendanceTask fetchAttendanceTask = new FetchAttendanceTask();
                fetchAttendanceTask.execute(new String[]{netId,password});
            }
        };
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
                List<WifiConfiguration> wifiInfos = mWifiManager.getConfiguredNetworks();
                for (WifiConfiguration configuration : wifiInfos) {
                    if (configuration.SSID.equals("Student")) {
                        mWifiManager.disconnect();
                        mWifiManager.enableNetwork(configuration.networkId,true);
                        isConnected = mWifiManager.reconnect();
                    }
                }
            }
        }
    }

    private class FetchAttendanceTask extends AsyncTask<String,Void,ArrayList<AttendanceData>> {
        private final String TAG = FetchAttendanceTask.class.getSimpleName();

        private String htmlContentInStringFormat;
        private CharSequence styledText;
        private Float displayed;
        private Float course_credit;
        private Float number1;
        private Float total;
        private int flag3;
        private Float total_credits;
        private String woaw;

        @Override
        public void onPreExecute(){
            Log.d(TAG,"onPreExecute() executing");
            if(!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }
            if(netId.length() == 0 || password.length() == 0) {
                cancel(true);
            }
            total = 0f;
            total_credits = 0f;
        }


        @Override
        protected ArrayList<AttendanceData> doInBackground(String... credentials) {
            if(isConnected) {
                Log.d(TAG, "doInBackground() executing");
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
                    Connection.Response login = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/login/loginAuthSubmit")
                            .data("login_user_name", credentials[0])
                            .data("login_password", credentials[1])
                            .userAgent("Mozilla")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .method(Connection.Method.POST)
                            .execute();

                    Connection.Response checkAttendance = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/index/summary")
                            .userAgent("Mozilla")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .method(Connection.Method.POST)
                            .cookies(login.cookies())
                            .execute();
                    Document doc = checkAttendance.parse();
                    try {
                        String x = " ";
                        Elements panelElements = doc.getElementsByClass("panel panel-primary");
                        Elements trElements = panelElements.select("tr");
                        for (int i = 0; i < trElements.size() - 1; i++) {
                            Element trElement = trElements.get(i + 1);
                            Elements tdElements = trElement.select("td");
                            x = x + tdElements.get(0).text() + "  -  " + tdElements.get(1).text() + "  -  ";
                            try {
                                number1 = Float.parseFloat(tdElements.get(6).text());
                                if (number1 < 75.0)
                                    x = x + "<b> <font color = #ff0000>" + tdElements.get(6).text() + "</font> </b>" + "<br>";
                                else
                                    x = x + "<b><font color = #13c000>" + tdElements.get(6).text() + "</font> </b>" + "<br>";

                            } catch (Exception e) {
                                x = x + tdElements.get(6).text() + "<br>";
                            }
                            x = x.replace("-", ":");
                        }
                        htmlContentInStringFormat = x;
                        styledText = Html.fromHtml(htmlContentInStringFormat);
                    }
                    catch (Exception e)
                    {
                        styledText = "Something went wrong.";
                    }
                    SharedPreferences prefs;
                    SharedPreferences.Editor editor;
                    prefs = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    editor = prefs.edit();
                    String stringToBeSaved;
                    stringToBeSaved = "Probably not connected to Student Wifi <br> <b> <font color = #ff0000>  ATTENDANCE AS LAST VIEWED :</font> </b> <br> <br> " + htmlContentInStringFormat;
                    editor.putString("attendance", stringToBeSaved);
                    editor.apply();
                } catch(Exception exception){
                    Log.d(TAG,"Exception:- " + exception.getMessage());
                    styledText = "ffff";
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(ArrayList<AttendanceData> response){
            Log.d(TAG,"onPostExecute() executing");
            if(swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            String stringTobeShown;
            SharedPreferences prefs;
            prefs = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            woaw = prefs.getString("attendance_credit", "");
            Log.e("#","j" + woaw);
            if(styledText.equals("ffff"))
            {
                if(woaw.equals(null))
                {
                    stringTobeShown = "Please connect to Student Wifi.";
                }
                else
                {
                    stringTobeShown = woaw;
                }
            }
            else
            {
                stringTobeShown = htmlContentInStringFormat;
            }
            styledText = Html.fromHtml(stringTobeShown);
        }
    }

    private static class AttendanceData{

    }
}
