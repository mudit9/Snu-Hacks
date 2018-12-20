package mu.snuhacks;
 /*
<color name="white">#DBFCFF</color>
    <color name="black">#788A8C</color>
    */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;

import static android.R.attr.button;
import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by mudit on 12/9/17.
 */

public class firstActivity extends AppCompatActivity {
    private final String TAG = "HomeActivity";
    public Context mContext = firstActivity.this;

    private SharedPreferences prefs;

    TextView parsedHtmlNode;
    String password;
   // Bundle extras = getIntent().getExtras();
    String username;
    public int ACTIVITY_NUM = 2;
    ScrollView scrollview_news;
    private InterstitialAd mInterstitialAd;
    CardView cardView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("MyPref", 0);
        username = prefs.getString("username","");
        password = prefs.getString("password","");
        setContentView(R.layout.firstscreen_test);
     //   if (extras != null) {
      //      username = extras.getString("username");
     //       password = extras.getString("password");
     //   }
        setupBottomNavigationView();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2461190858191596/4980119936");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
      //  FancyButton messMenu1 = (FancyButton) findViewById(R.id.messMenuButton);
        FancyButton data_btn = (FancyButton) findViewById(R.id.data_button);
        parsedHtmlNode = (TextView) findViewById(R.id.welcome5);
        FancyButton aboutbutton = (FancyButton) findViewById(R.id.aboutButton);
        scrollview_news = findViewById(R.id.scrollView_news);
       // FancyButton LaundryButton = (FancyButton) findViewById(R.id.laundryButton);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/ChiselMark.ttf");
        parsedHtmlNode.setTypeface(custom_font);
        FancyButton attendanceButton = (FancyButton) findViewById(R.id.attendance_button);
        FancyButton logoutButton = (FancyButton) findViewById(R.id.logout_button);
        attendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startAttendance = new Intent(firstActivity.this, Attendance.class);
                startActivity(startAttendance);
            }
        });
        data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startData = new Intent(firstActivity.this, DataUsage.class);
                startActivity(startData);
            }
        });
     /*   messMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startData = new Intent(firstActivity.this, messMenu.class);
                startActivity(startData);
            }
        });*/
        aboutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startAbout = new Intent(firstActivity.this, About.class);
                startActivity(startAbout);
            }
        });
     /*   LaundryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startAbout = new Intent(firstActivity.this, Laundry.class);
                startActivity(startAbout);
            }
        });
*/      setScrollview_news();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    public void setScrollview_news(){
        TextView heading;
        TextView subtext;
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i  = 0; i <8; i++) {
            FrameLayout frameLayout = (FrameLayout) View.inflate(this, R.layout.content_card, null);
            heading = frameLayout.findViewById(R.id.meal);
            subtext = frameLayout.findViewById(R.id.menu);
            heading.setText("heading "+ i);
            subtext.setText("subtext "+ i);
            frameLayout.setPadding(0,5,0,0);
            linearLayout.addView(frameLayout);
        }
        scrollview_news.addView(linearLayout);
    }

    public void logout() {
        SharedPreferences prefs = getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor;
        editor = prefs.edit();
        editor.clear();
        editor.apply(); // commit changes
        Intent log_out = new Intent(firstActivity.this,MainActivity.class);
        startActivity(log_out);
        finish();
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



}

