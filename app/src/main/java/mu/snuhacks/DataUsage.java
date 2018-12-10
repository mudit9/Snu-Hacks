package mu.snuhacks;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;
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

import mehdi.sakout.fancybuttons.FancyButton;

import static android.R.id.progress;

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
    TextView Dataaa;
    AVLoadingIndicatorView avi2;
    //Bundle extras = getIntent().getExtras();
    Button data_button;
    String password;
    private final String TAG = "HomeActivity";
    public Context mContext = DataUsage.this;
    Integer usage3;
    String netId;
    public int ACTIVITY_NUM = 3;
    Float use;
    int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   if (extras != null) {
     //       netId = extras.getString("username");
      //      password = extras.getString("password");
     //   }
        setContentView(R.layout.data_usage);
        SharedPreferences prefs = getSharedPreferences("MyPref", 0);
        netId = prefs.getString("username","");
        password = prefs.getString("password","");
        avi1 = (AVLoadingIndicatorView) findViewById(R.id.avielement1);
        System.out.println(netId + " f " + password);
        Dataaa = (TextView) findViewById(R.id.DATAUSAGE);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/ChiselMark.ttf");
        Dataaa.setTypeface(custom_font);
        parsedHtmlNode = (TextView) findViewById(R.id.welcome);
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/LifeSaver.ttf");
        parsedHtmlNode.setTypeface(custom_font2);
        setupBottomNavigationView();
        Circleprogress = (CircleProgress) findViewById(R.id.circle_progress);
        Circleprogress.setVisibility(View.GONE);
        avi2 = (AVLoadingIndicatorView) findViewById(R.id.avielement2);
        FancyButton data_button = (FancyButton) findViewById(R.id.data_usage_button);
        data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();

            }
        });
    }
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_viewbar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,this , bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            avi1.smoothToShow();;
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
            avi1.smoothToHide();;
            parsedHtmlNode.setText(htmlContentInStringFormat);
            try{
            Circleprogress.setProgress(usage3);
            Circleprogress.setVisibility(View.VISIBLE);}
            catch (Exception e){
                parsedHtmlNode.setText("Probably not connected to Student Wifi");
            }
            if(flag == 1)
                parsedHtmlNode.setText("Probably not connected to Student Wifi");
            if(flag == 2)
            {
                parsedHtmlNode.setText("Something went  wrong :( ");
            }
            parsedHtmlNode.setMovementMethod(new ScrollingMovementMethod());
        }
    }
}
