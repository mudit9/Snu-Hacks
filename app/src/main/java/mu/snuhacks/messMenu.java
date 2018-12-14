package mu.snuhacks;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
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
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;

import static android.R.id.progress;

/**
 * Created by mudit on 12/9/17.
 */

public class messMenu extends AppCompatActivity {
    TextView parsedHtmlNode;
    String htmlContentInStringFormat;
    FancyButton dh1;
    FancyButton dh2;
    TextView menu;
    String password;
    private final String TAG = "HomeActivity";
    public Context mContext = messMenu.this;
    AVLoadingIndicatorView avi2;
    Integer usage3;
    String netId;
    CharSequence styledText;
    Float use;
    AVLoadingIndicatorView avi3;
    public int ACTIVITY_NUM = 4;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   if (extras != null) {
        //       netId = extras.getString("username");
        //      password = extras.getString("password");
        //   }
        setContentView(R.layout.messmenu);
        SharedPreferences prefs = getSharedPreferences("MyPref", 0);
        netId = prefs.getString("username","");
        password = prefs.getString("password","");
        FancyButton dh1 = (FancyButton) findViewById(R.id.dh11);
        FancyButton dh2 = (FancyButton) findViewById(R.id.dh22);
        avi2= (AVLoadingIndicatorView) findViewById(R.id.avielement2);
        avi3 = (AVLoadingIndicatorView) findViewById(R.id.avielement3);
        menu = (TextView) findViewById(R.id.menu1);
        setupBottomNavigationView();
        parsedHtmlNode = (TextView) findViewById(R.id.welcome7);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/ChiselMark.ttf");
        parsedHtmlNode.setTypeface(custom_font);
        ;
        dh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();

            }
        });
        dh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsoupAsyncTask2 jsoupAsyncTask2 = new JsoupAsyncTask2();
                jsoupAsyncTask2.execute();

            }
        });
    }
    /**
     * BottomNavigationView setup
     */
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
            avi2.smoothToShow();
            menu.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String breakfast="c";
                String lunch="";
                String dinner="";
                Connection.Response response = Jsoup.connect("http://messmenu.snu.in/messMenu.php")
                        .method(Connection.Method.POST)
                        .execute();
                //parse the document from response
                try {
                    Document document2 = response.parse();
                    Element elementsByTag = document2.getElementsByClass("table table-striped table-bordered table-hover").get(0);
                    Element elementsByTags = elementsByTag.getElementsByTag("tbody").get(0);
                    Element elementsByTags1 = elementsByTags.getElementsByTag("tr").get(0);
                    Elements elementByTags2 = elementsByTags1.getElementsByTag("td");
                    System.out.println(elementByTags2.get(1).toString());
                    breakfast
                            = elementByTags2.get(1).text();
                    lunch = elementByTags2.get(2).text();
                    dinner = elementByTags2.get(3).text();
                    htmlContentInStringFormat = "<b>" + "Breakfast" + "</b>" + "<br>" +breakfast + "<br>" + "<b> " + "Lunch" + "</b> " + "<br>" + lunch + "<br>" + "<b>" + "Dinner" + "</b> " + "<br>" + dinner;
                    styledText = Html.fromHtml(htmlContentInStringFormat);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    styledText = "No menu available!";

                }
            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            avi2.smoothToHide();
            menu.setText(styledText);
            menu.setVisibility(View.VISIBLE);
            menu.setMovementMethod(new ScrollingMovementMethod());

        }
    }
    private class JsoupAsyncTask2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            avi3.smoothToShow();
            menu.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String breakfast="";
                String lunch="";
                String dinner="";
                Connection.Response response = Jsoup.connect("http://messmenu.snu.in/messMenu.php")
                        .method(Connection.Method.POST)
                        .execute();
                //parse the document from response
                try {
                    Document document2 = response.parse();
                    Element elementsByTag = document2.getElementsByClass("table table-striped table-bordered table-hover").get(1);
                    Element elementsByTags = elementsByTag.getElementsByTag("tbody").get(0);
                    Element elementsByTags1 = elementsByTags.getElementsByTag("tr").get(0);
                    Elements elementByTags2 = elementsByTags1.getElementsByTag("td");
                    breakfast = elementByTags2.get(1).text();
                    lunch = elementByTags2.get(2).text();
                    dinner = elementByTags2.get(3).text();
                    htmlContentInStringFormat = "<b>" + "Breakfast" + "</b>" + "<br>" +breakfast + "<br>" + "<b> " + "Lunch" + "</b> " + "<br>" + lunch + "<br>" + "<b>" + "Dinner" + "</b> " + "<br>" + dinner;
                    styledText = Html.fromHtml(htmlContentInStringFormat);
                }
                catch (Exception e){
                    e.printStackTrace();
                    styledText = "No menu available!";

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            avi3.smoothToHide();
            menu.setText(styledText);
            menu.setVisibility(View.VISIBLE);
            menu.setMovementMethod(new ScrollingMovementMethod());
        }
    }
}
