package mu.snuhacks;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.cert.CertificateException;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mehdi.sakout.fancybuttons.FancyButton;

//import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity ";
    private Handler handler;
    private SharedPreferences prefs;
    private String NetId = null, password = null;
    public String nam2;
    public SharedPreferences.Editor editor;
    public String[] messages = {"Aellow Ho raha hai... ","Loading.."};
    public Context mContext = MainActivity.this;
    // private String name=null;
    private EditText etNetId, etPass;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        NetId = prefs.getString("username", null);
        password = prefs.getString("password", null);
        MobileAds.initialize(this, "ca-app-pub-2461190858191596~7989426659");

        //mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");


        if(NetId != null && password != null){
            Intent startFirst = new Intent(MainActivity.this, firstActivity.class);
            startActivity(startFirst);
            finish();
        }

        setContentView(R.layout.activity_main);
        etNetId = (EditText) findViewById(R.id.username);
        etPass = (EditText) findViewById(R.id.password);
        FancyButton btLogin = (FancyButton) findViewById(R.id.login_button);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetId = etNetId.getText().toString();
                password = etPass.getText().toString();
                if(isValid()) {
                    confirmDetails();
                }
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1) {
                    etNetId.setError("Invalid NetId");
                    Toast.makeText(MainActivity.this, "Please check your NetID and password", Toast.LENGTH_SHORT).show();
                }
                else if (msg.what == 2) {
                    etPass.setError("Invalid password or ID or both.");
                    Toast.makeText(MainActivity.this, "Please check your NetID and password", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

       // TextView tvCredit = (TextView) findViewById(R.id.textView2);
        //tvCredit.setText(Html.fromHtml(getString(R.string.credit)));
       // tvCredit.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private boolean isValid() {
        if(NetId.length() != 5) {
            etNetId.setError("Invalid NetID");
            return false;
        }
        if(password.length() == 0) {
            etPass.setError("Empty password");
            return false;
        }

        return true;
    }

    Call post(Callback callback) throws IOException {
        OkHttpClient client = getUnsafeOkHttpClient();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);
        RequestBody requestBody = new FormEncodingBuilder()
                .add("user_id", NetId)
                .add("user_password", password)
                .build();
  //      Request request = new Request.Builder()
        //            .url("https://studentmaintenance.webapps.snu.edu.in/students/public/studentslist/studentslist/loginauth")
  //              .post(requestBody)
    //            .build();
        Request request = new Request.Builder()
                   .url("https://hms.webapps.snu.edu.in/hostel/public/snuhostel/hostel/registerroom")
                 .post(requestBody)
                  .build();
        Log.d("a",request.toString());


        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    private void confirmDetails() {
        Random ran = new Random();
        int xa = ran.nextInt(2);
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //dialog.setMessage(messages[xa]);
        dialog.setMessage(messages[0]);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        try {
            post(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e(TAG, "Details confirmation failed. " + e.toString());
                    dialog.dismiss();
                }


                @Override
                public void onResponse(Response response) throws IOException {
                    System.out.println(response.toString());
                    /**
                     * The student details website uses class "label label-danger"
                     * to show error in authentication. If this class is refereed
                     * then there is an error authenticating the user.
                     */

                    Connection.Response html = Jsoup.connect("https://hms.webapps.snu.edu.in/hostel/public/login/")
                            .data("UserID", NetId)
                            .data("Password", password)
                            .userAgent("Mozilla")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .method(Connection.Method.POST)
                            .execute();

                    Document doc = html.parse();
                    Elements a = doc.getElementsByClass("glyphicon glyphicon-user");
                    Elements b =  doc.getElementsByClass("pull-right");
                    Log.d("a",doc.toString());
                    Log.d("aaa",a.toString());
                    Log.d("AAAA",b.text());
                    String name2 = b.text();
                    String an = name2.replaceAll("Welcome", "");
                    String name = an.replaceAll("[Logout]", "");
                    name = name.trim();
                    Log.d("name",name);
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", 0);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference nameReference = database.getReference("/data/fcm_tokens_name/" + preferences.getString("fcm_key",""));
                    nameReference.setValue(preferences.getString("name","no_name")).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG,"Name corresponding to FCM token saved");
                            }
                        }
                    });
                    nam2 = name2.substring(8, name2.length()-9);
                    name2 = nam2.trim();
                    Log.d("name",nam2);

                    dialog.dismiss();
                //    Log.d("#","BLAH" + doc.toString());
                   String responseData = response.toString();
                 //=   Log.d("#",response.toString());
                  //  System.out.println(responseData);
                   // System.out.println(doc);
                    if (doc.toString().contains("Please use SNU Net Id and Password to login")) {
                        Log.e(TAG, "Bad login credentials");
                        /**
                         * If true then wrong SNU Net ID.
                         * Else wrong password.
                         */
                        if (responseData.contains("Please Enter SNU Net ID")) {
                            Message message = handler.obtainMessage(1);
                            message.sendToTarget();
                        } else {
                            Message message = handler.obtainMessage(2);
                            message.sendToTarget();
                        }
                        return;
                    }


                        SharedPreferences.Editor editor;
                        editor = prefs.edit();
                        editor.putString("username", NetId);
                        editor.putString("password", password);
                        editor.putString("name",nam2);
                        editor.apply();
                        Intent startFirst = new Intent(MainActivity.this, firstActivity.class);
                        startActivity(startFirst);
                        finish();

                }
            });
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}