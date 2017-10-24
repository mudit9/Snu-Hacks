package mu.snuhacks;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import mehdi.sakout.fancybuttons.FancyButton;


public class Attendance extends AppCompatActivity {



        private TextView parsedHtmlNode;
        String htmlContentInStringFormat;
        TextView AttendanceText;
        ImageView line1;
        String netId;
        String password;
        AVLoadingIndicatorView avi;
        CharSequence styledText;
        AVLoadingIndicatorView avi4;
        TextView meh11;
        Float number1;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            SharedPreferences prefs = getSharedPreferences("MyPref", 0);
            netId = prefs.getString("username","");
            password = prefs.getString("password","");
            setContentView(R.layout.attendance_layout);
            line1 = (ImageView) findViewById(R.id.line123);
            meh11 = (TextView) findViewById(R.id.meh13);
            avi4 = (AVLoadingIndicatorView) findViewById(R.id.avielement4);
            avi = (AVLoadingIndicatorView) findViewById(R.id.avielement);
            parsedHtmlNode = (TextView)findViewById(R.id.html_content);
            AttendanceText = (TextView)findViewById(R.id.textAtt);
            Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/ChiselMark.ttf");
            AttendanceText.setTypeface(custom_font);
            final FancyButton CheckAttendanceButton = (FancyButton) findViewById(R.id.check_attendance_button) ;
            final FancyButton MarkAttendanceButton = (FancyButton) findViewById(R.id.mark_attendance_button);
           // if (extras != null) {
            //    netId = extras.getString("username");
            //    password = extras.getString("password");
          //  }
            MarkAttendanceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JsoupAsyncTask2 jsoupAsyncTask2 = new JsoupAsyncTask2();
                    jsoupAsyncTask2.execute();
                }
            });
            CheckAttendanceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JsoupAsyncTask3 jsoupAsyncTask3 = new JsoupAsyncTask3();
                    jsoupAsyncTask3.execute();
                    MarkAttendanceButton.setVisibility(View.GONE);
                    CheckAttendanceButton.setVisibility(View.GONE);
                    meh11.setVisibility(View.GONE);
                }
            });
        }


    private class JsoupAsyncTask3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            avi.smoothToShow();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Response html = Jsoup.connect("https://markattendance.webapps.snu.edu.in/public/application/login/loginAuthSubmit")
                        .data("login_user_name", netId)
                        .data("login_password", password)
                        .userAgent("Mozilla")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .method(Connection.Method.POST)
                        .execute();

                    Document doc = html.parse();
                    //Element bttn = doc.getElementsByClass("btn btn-block btn-primary").last();
                    String x = " ";
                    Elements bttn = doc.select("tr");
                    for (int i = 0; i < bttn.size()-1; i++) {
                        Element meh = bttn.get(i + 1);
                        Elements meh2 = meh.select("td");
                        x = x + meh2.get(0).text() + "  -  " + meh2.get(1).text() + "  -  " ;
                        try {
                            number1 = Float.parseFloat(meh2.get(5).text());
                            if (number1 < 75.0)
                                x = x + "<b> <font color = #ff0000>" + meh2.get(5).text() + "</font> </b>" + "<br>";
                            else
                                x = x + "<b><font color = #13c000>" + meh2.get(5).text() + "</font> </b>" +"<br>";

                        }
                        catch (Exception e) {
                            x = x + meh2.get(5).text()+"<br>";
                        }
                        x=x.replace("-",":");

                    }

                htmlContentInStringFormat = x;
                styledText = Html.fromHtml(htmlContentInStringFormat);

            } catch (IOException e) {
                e.printStackTrace();
                styledText = "Probably not connected to Student Wifi.";
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            avi.smoothToHide();
            parsedHtmlNode.setText(styledText);
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


            } catch (IOException e) {
                htmlContentInStringFormat = "Probably not connected to Student Wifi.";
                e.printStackTrace();
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
  //  void startAnim(){
    //}

//    void stopAnim(){
  //  }
}





