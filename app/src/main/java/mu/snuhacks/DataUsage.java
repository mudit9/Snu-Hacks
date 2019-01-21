package mu.snuhacks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mudit on 12/9/17.
 */

public class DataUsage extends AppCompatActivity {
    private Document htmlDocument;

    private String htmlPageUrl = "https://myaccount.snu.edu.in/login.php";
    protected TextView parsedHtmlNode;
    String htmlContentInStringFormat;
    AVLoadingIndicatorView avi1;
    CircleProgress Circleprogress;
    //Bundle extras = getIntent().getExtras();
    private SharedPreferences prefs;
    Button data_button;
    String password;
    private final String TAG = "HomeActivity";
    public Context mContext = DataUsage.this;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    TextView head;
    Integer usage3;
    String netId;
    public int ACTIVITY_NUM = 3;
    Float use;
    int flag = 0;
    ProgressBar mProgress;
    Button data;
    private InterstitialAd mInterstitialAd;
    SharedPreferences.Editor editor;
    private boolean isConnected;
    WifiManager mWifiManager;
    TextView emptyTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_usage_test);
        mProgress = findViewById(R.id.ProgressBar1);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        emptyTextView = findViewById(R.id.empty_text_view);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2461190858191596/4980119936");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        data = findViewById(R.id.data_button);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh2);
        head = findViewById(R.id.heading);
        prefs = getSharedPreferences("MyPref", 0);
        netId = prefs.getString("username", "");
        password = prefs.getString("password", "");
        System.out.println(netId + " f " + password);
        //parsedHtmlNode = findViewById(R.id.welcome);
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Junction-regular.otf");
        head.setTypeface(custom_font2);
        head.setPadding(10,5,0,0);
        setupBottomNavigationView();

        Circleprogress = findViewById(R.id.circle_progress);
        Circleprogress.setVisibility(View.GONE);

        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();

                Log.d(TAG,"onRefresh() called");

                checkAndModifyWifiState();
                if(isConnected == true){

                    jsoupAsyncTask.execute();
                }
                else {emptyTextView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        };
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

       data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (mInterstitialAd.isLoaded()) {
                   mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

            }
        });
        data.setVisibility(View.GONE);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DataUsage.this);

        Log.d("Wifi name: ",mWifiManager.getConnectionInfo().getSSID());
        if(mWifiManager != null && !isConnected){
            Log.d("Wifi name: ",mWifiManager.getConnectionInfo().getSSID());
            if(!mWifiManager.isWifiEnabled()){
                Log.d(TAG,"Wifi enabled");
                builder.setTitle("Connect to Student Wifi?");
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


    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_viewbar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx,getApplicationContext());
        BottomNavigationViewHelper.enableNavigation(mContext,this , bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress.setIndeterminate(true);
            mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar date = Calendar.getInstance();
                String today = df.format(date.getTime());
                int diff = (date.get(Calendar.DAY_OF_WEEK) - Calendar.WEDNESDAY) % 7;
                if (!(diff >= 0)) {
                    diff += 7;
                }
                date.add(Calendar.DAY_OF_MONTH, (-diff));
                String last_wednesday = df.format(date.getTime());
                Connection.Response response = Jsoup.connect("https://myaccount.snu.edu.in/loginSubmit.php")
                        .data("snuNetId", netId)
                        .data("password", password)
                        .data("Submit", "Login")
                        .method(Connection.Method.POST)
                        .execute();
                //parse the document from response
                Document document2 = response.parse();

                Connection.Response document = Jsoup.connect("https://myaccount.snu.edu.in/myAccountInfo.php")
                        .cookies(response.cookies())
                        .data("startDate", last_wednesday)
                        .data("endDate", today)
                        .data("submit", "Submit")
                        .method(Connection.Method.POST)
                        .execute();


                Document document1 = document.parse();
                Element elementsByTag = document1.getElementsByTag("tfoot").get(0);
                String usage = elementsByTag.getElementsByTag("th").last().text().substring(13);
                Float usage1 = Float.parseFloat(usage);
                use = usage1*100/3;
                usage3=(Math.round(use));
                if(usage3>=100) {
                 usage3=100;
                }
                htmlContentInStringFormat = (usage1 + " gb.");

            } catch (IOException e) {
                e.printStackTrace();
                flag = 1;
            }
            catch(Exception e)
            {
                flag = 2;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try{
            emptyTextView.setText(htmlContentInStringFormat.toUpperCase());}
            catch (Exception e){

            }
            if(emptyTextView.getVisibility()==View.GONE)
                emptyTextView.setVisibility(View.VISIBLE);
            try{
            Circleprogress.setProgress(usage3);
                swipeRefreshLayout.setRefreshing(false);
                Circleprogress.setVisibility(View.VISIBLE);}
            catch (Exception e){
                emptyTextView.setText("Probably not connected to Student Wifi \n Pull to refresh!");
            }
            if(flag == 1)
                emptyTextView.setText("Probably not connected to Student Wifi \n Pull to refresh!");
            if(flag == 2)
            {
                emptyTextView.setText("Something went  wrong :( \n Pull to refresh!");
            }
            emptyTextView.setMovementMethod(new ScrollingMovementMethod());
            mProgress.setIndeterminate(false);
            mProgress.setVisibility(View.GONE);
            editor = prefs.edit();
            editor.putString("data_usage",htmlContentInStringFormat);
            editor.apply();

        }
    }
}
