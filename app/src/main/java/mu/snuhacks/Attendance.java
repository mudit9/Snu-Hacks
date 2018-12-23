package mu.snuhacks;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mehdi.sakout.fancybuttons.FancyButton;


public class Attendance extends AppCompatActivity {



        private TextView parsedHtmlNode;
        String htmlContentInStringFormat;
        TextView AttendanceText;
        ImageView line1;
        TextView active;
        String netId;
        Float displayed;
        String password;
        AVLoadingIndicatorView avi;
        CharSequence styledText;
        AVLoadingIndicatorView avi4;
        TextView meh11;
        int flag2;
        Float course_credit;
        public int ACTIVITY_NUM = 1;
    private final String TAG = "HomeActivity";
    public Context mContext = Attendance.this;
        Float number1;
        Float total;
        String woaw;
        int flag3;
        Float total_credits;



        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            SharedPreferences prefs = getSharedPreferences("MyPref", 0);
            netId = prefs.getString("username","");
            password = prefs.getString("password","");
            setContentView(R.layout.attendance_layout);
            total = 0f;
            total_credits = 0f;
            line1 = (ImageView) findViewById(R.id.line123);
            meh11 = (TextView) findViewById(R.id.meh13);
            avi4 = (AVLoadingIndicatorView) findViewById(R.id.avielement4);
            avi = (AVLoadingIndicatorView) findViewById(R.id.avielement);
            parsedHtmlNode = (TextView)findViewById(R.id.html_content);
            setupBottomNavigationView();
            active = (TextView) findViewById(R.id.activenow);
            AttendanceText = (TextView)findViewById(R.id.textAtt);
            Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/ChiselMark.ttf");
            AttendanceText.setTypeface(custom_font);
            final FancyButton CheckAttendanceButton = (FancyButton) findViewById(R.id.check_attendance_button) ;
            final FancyButton MarkAttendanceButton = (FancyButton) findViewById(R.id.mark_attendance_button);
            final FancyButton CheckAttendanceCreditButton = (FancyButton) findViewById(R.id.check_credit_attendance);
            prefs = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            woaw = prefs.getString("attendance", "");
        //    active.setVisibility(View.GONE);

           // JsoupAsyncTask4 jsoupAsyncTask4 = new JsoupAsyncTask4();
          //  jsoupAsyncTask4.execute();
            MarkAttendanceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JsoupAsyncTask2 jsoupAsyncTask2 = new JsoupAsyncTask2();
                    jsoupAsyncTask2.execute();
                }
            });
            CheckAttendanceCreditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JsoupAsyncTask5 jsoupAsyncTask5 = new JsoupAsyncTask5();
                    jsoupAsyncTask5.execute();
                    MarkAttendanceButton.setVisibility(View.GONE);
                    CheckAttendanceButton.setVisibility(View.GONE);
                    CheckAttendanceCreditButton.setVisibility(View.GONE);
                //    try{active.setVisibility(View.GONE);}
               //     catch(Exception c){};
                    meh11.setVisibility(View.GONE);

                }
            });
            CheckAttendanceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JsoupAsyncTask3 jsoupAsyncTask3 = new JsoupAsyncTask3();
                    jsoupAsyncTask3.execute();
                    MarkAttendanceButton.setVisibility(View.GONE);
                    CheckAttendanceButton.setVisibility(View.GONE);
                    CheckAttendanceCreditButton.setVisibility(View.GONE);
                    meh11.setVisibility(View.GONE);
               //     try{active.setVisibility(View.GONE);}
              //      catch(Exception c){};
                }
            });
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


    private class JsoupAsyncTask3 extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            avi.smoothToShow();
            SharedPreferences prefs;
            SharedPreferences.Editor editor;
            try{active.setVisibility(View.GONE);}
            catch(Exception c){};
         /*   if(woaw.equals(""))
           {
               woaw = woaw + "<br> Might not be refreshed. Might be old data.";
               Log.e("#","wedededed4d");
            //    System.out.print(woaw);
                parsedHtmlNode.setText(Html.fromHtml(woaw));
            }
            */
          //  else
          //      Log.e("#","afafaf");
        }

        @Override
        protected Void doInBackground(Void... params) {
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
                    throw   new RuntimeException(e);
                }

                Response html = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/login/loginAuthSubmit")
                        .data("login_user_name", netId)
                        .data("login_password", password)
                        .userAgent("Mozilla")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .method(Connection.Method.POST)
                        .execute();

                Response html2 = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/index/summary")
                        .userAgent("Mozilla")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .method(Connection.Method.POST)
                        .cookies(html.cookies())
                        .execute();



                        Document doc = html2.parse();
                        Log.e("doc", doc.toString());
                        try {
                            //Element bttn = doc.getElementsByClass("btn btn-block btn-primary").last();
                            String x = " ";
                            Elements bttn2 = doc.getElementsByClass("panel panel-primary");
                            Elements bttn = bttn2.select("tr");
                            Log.d("bttn", bttn.toString());
                            for (int i = 0; i < bttn.size() - 1; i++) {
                                Element meh = bttn.get(i + 1);
                                Log.d("meh", meh.toString());
                                //Elements
                                Elements meh2 = meh.select("td");
                                Log.d("tag", meh2.toString());
                                x = x + meh2.get(0).text() + "  -  " + meh2.get(1).text() + "  -  ";
                                try {
                                    Log.e("TEXTTTTT", meh2.get(6).text());
                                    number1 = Float.parseFloat(meh2.get(6).text());
                                    if (number1 < 75.0)
                                        x = x + "<b> <font color = #ff0000>" + meh2.get(6).text() + "</font> </b>" + "<br>";
                                    else
                                        x = x + "<b><font color = #13c000>" + meh2.get(6).text() + "</font> </b>" + "<br>";

                                } catch (Exception e) {
                                    x = x + meh2.get(6).text() + "<br>";
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
                prefs = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                editor = prefs.edit();
                String stringToBeSaved;
                stringToBeSaved = "Probably not connected to Student Wifi <br> <b> <font color = #ff0000>  ATTENDANCE AS LAST VIEWED :</font> </b> <br> <br> " + htmlContentInStringFormat;
                editor.putString("attendance", stringToBeSaved);
                editor.apply();

            } catch (IOException e) {
                e.printStackTrace();
               styledText = "ffff";
               // styledText = "ff";
                Log.e("#",e.toString());

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            avi.smoothToHide();
            String stringTobeShown;
            SharedPreferences prefs;
            prefs = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            woaw = prefs.getString("attendance", "");
            try{active.setVisibility(View.GONE);}
            catch(Exception c){};
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
            parsedHtmlNode.setText(styledText);

         /*


            if(!styledText.equals("Probably not connected to Student Wifi.")) {
                parsedHtmlNode.setText(Html.fromHtml(woaw));
            }

            else {
                if(woaw!=null)
                {   styledText = woaw;
                    styledText = styledText + "<br> Might not be refreshed. Might be old data.";
                    Log.e("#","wedededed4d");
                    //    System.out.print(woaw);
                    parsedHtmlNode.setText(Html.fromHtml(styledText.toString()));
                }
                else
                {
                    styledText = "Something went wrong.";
                    parsedHtmlNode.setText(styledText);
                }
            }
        */
            parsedHtmlNode.setMovementMethod(new ScrollingMovementMethod());

        }
    }
    private class JsoupAsyncTask2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            avi4.smoothToShow();
        }

        @Override
        protected Void doInBackground(Void... params) {
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

                Response html = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/login/loginAuthSubmit")
                        .data("login_user_name", netId)
                        .data("login_password", password)
                        .userAgent("Mozilla")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .method(Connection.Method.POST)
                        .execute();

                Response html2 = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/index/submit_attendance")
                        .userAgent("Mozilla")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .method(Connection.Method.POST)
                        .cookies(html.cookies())
                        .execute();

                Document doc = html2.parse();
                Element alert = doc.getElementsByClass("alert alert-warning alert-dismissible").first();
                Element success = doc.getElementsByClass("alert alert-success alert-dismissible").first();
                if(alert!=null)
                    htmlContentInStringFormat ="<br> <br> <font color = #ff0000>" + alert.text() + "</font>";
                else if(success!=null)
                    htmlContentInStringFormat ="<br> <br> <font color = #13c000>" + success.text() + "</font>";
                else
                    htmlContentInStringFormat = "Failed.";
            }
            catch (IOException e) {
                htmlContentInStringFormat = "Probably not connected to Student Wifi.";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            avi4.smoothToHide();
            parsedHtmlNode.setText(Html.fromHtml(htmlContentInStringFormat));
            parsedHtmlNode.setMovementMethod(new ScrollingMovementMethod());

        }

    }
//    private class JsoupAsyncTask4 extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            active.setText("Checking...");
//            try {
//                active.setTextColor(getResources().getColor(R.color.white));
//                active.setVisibility(View.VISIBLE);
//            }
//            catch (Exception e)
//            {
//                //
//            }
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//
//                Response html = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/index/summary")
//                        .data("login_user_name", netId)
//                        .data("login_password", password)
//                        .userAgent("Mozilla")
//                        .header("X-Requested-With", "XMLHttpRequest")
//                        .method(Connection.Method.POST)
//                        .execute();
//
//                Document doc = html.parse();
//                if(doc.toString().contains("Submit"))
//                flag2 = 1;
//
//            } catch (Exception e) {
//
//            }
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Void result) {
//
//            if(flag2 ==1) {
//                active.setText("Active.");
//                active.setTextColor(getResources().getColor(R.color.green));
//                active.setVisibility(View.VISIBLE);
//            }
//
//            else {
//                active.setText("Not active.");
//                active.setTextColor(getResources().getColor(R.color.red));
//                active.setVisibility(View.VISIBLE);
//            }
//                // else
//              //  active.setVisibility(View.GONE);
//        }
//
//    }
    private class JsoupAsyncTask5 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            avi.smoothToShow();
            total = 0f;
            total_credits = 0f;
            SharedPreferences prefs;
            SharedPreferences.Editor editor;
           // try{active.setVisibility(View.GONE);}
            //catch(Exception c){};

         /*   if(woaw.equals(""))
           {
               woaw = woaw + "<br> Might not be refreshed. Might be old data.";
               Log.e("#","wedededed4d");
            //    System.out.print(woaw);
                parsedHtmlNode.setText(Html.fromHtml(woaw));
            }
            */
            //  else
            //      Log.e("#","afafaf");
        }

        @Override
        protected Void doInBackground(Void... params) {
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

                Response html = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/login/loginAuthSubmit")
                        .data("login_user_name", netId)
                        .data("login_password", password)
                        .userAgent("Mozilla")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .method(Connection.Method.POST)
                        .execute();

                Response html2 = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/index/crs_wise_att_ch")
                        .userAgent("Mozilla")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .method(Connection.Method.POST)
                        .cookies(html.cookies())
                        .execute();

                Document doc = html2.parse();
                //Element bttn = doc.getElementsByClass("btn btn-block btn-primary").last();
              //  Log.e("#",doc.toString());
                String x = " ";
                Elements bttn = doc.select("tr");
                for (int i = 1; i < bttn.size()-2; i++) {
                    Element meh = bttn.get(i + 1);
                //    Log.e("#",meh.toString());
                    Elements meh2 = meh.select("td");
                    Log.e("#",meh2.toString());
                    x = x + meh2.get(0).text(   ) + "  -  " ;
                    course_credit = Float.parseFloat(meh2.get(1).text()) + Float.parseFloat(meh2.get(2).text()) + Float.parseFloat(meh2.get(3).text());
                    Log.e("coruse credit ;",course_credit.toString());
                    try {
                        String wowwow = meh2.get(14).text().replace("%","");
                        Log.d("wowow",wowwow.toString());
                        number1 = Float.parseFloat(wowwow);
                        course_credit = Float.parseFloat(meh2.get(1).text()) + Float.parseFloat(meh2.get(2).text()) + Float.parseFloat(meh2.get(3).text());
                        total_credits = total_credits + course_credit;
                        total = total + (course_credit*number1);
                        Log.e("Total credits :",total_credits.toString());
                        Log.e("total = ",total.toString());
                        if (number1 < 75.0) {
                            x = x + "<b> <font color = #ff0000>" + meh2.get(14).text() + "</font> </b>" + "<br>";
                        }
                        else
                            x = x + "<b><font color = #13c000>" + meh2.get(14).text() + "</font> </b>" +"<br>";

                    }
                    catch (Exception e) {
                        try {
                            x = x + meh2.get(14).text() + "<br>";
                        } catch (Exception E)
                        { Log.e("#",E.toString());
                        }
                    }
                    x=x.replace("-",":");
                    displayed = total/total_credits;
                    Log.e("dissplayed : ", displayed.toString());
                    displayed = Float.valueOf(String.format("%.2f", displayed));
                    if(displayed<75.0)
                        flag3 = 0;
                    else
                        flag3 = 1;

                }

                htmlContentInStringFormat = x;
                if(flag3 == 0) {
                    htmlContentInStringFormat = htmlContentInStringFormat + "<br> <font color = #ff0000> Overall attendance percentage = <b>" + displayed.toString() + "%</b></font>";
                }
                else
                {
                    htmlContentInStringFormat = htmlContentInStringFormat + "<br> <font color = #13c000> Overall attendance percentage = <b>" + displayed.toString() + "%</b></font>";

                }
                styledText = Html.fromHtml(htmlContentInStringFormat);
                SharedPreferences prefs;
                SharedPreferences.Editor editor;
                prefs = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                editor = prefs.edit();
                String stringToBeSaved;
                stringToBeSaved = "Probably not connected to Student Wifi <br> <b> <font color = #ff0000>  ATTENDANCE AS LAST VIEWED :</font> </b> <br> <br> " + htmlContentInStringFormat;
                editor.putString("attendance_credit", stringToBeSaved);
                editor.apply();


            } catch (IOException e) {
                //    e.printStackTrace();
                //  styledText = "Probably not connected to Student Wifi.";
                styledText = "ffff";
                Log.e("#","Catch going in");

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

            avi.smoothToHide();
            String stringTobeShown;
            SharedPreferences prefs;
            prefs = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            woaw = prefs.getString("attendance_credit", "");
           // try{active.setVisibility(View.GONE);}
           // catch(Exception c){};
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
            parsedHtmlNode.setText(styledText);
            parsedHtmlNode.setMovementMethod(new ScrollingMovementMethod());

        }
        }
    }

  //  void startAnim(){
    //}

//    void stopAnim(){
  //  }






