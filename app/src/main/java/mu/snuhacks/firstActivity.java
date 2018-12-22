package mu.snuhacks;
 /*
<color name="white">#DBFCFF</color>
    <color name="black">#788A8C</color>
    */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;
import mu.snuhacks.Adapters.NewsletterAdapter;

import static android.R.attr.button;
import static android.R.attr.colorAccent;
import static android.R.attr.data;
import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by mudit on 12/9/17.
 */

public class firstActivity extends AppCompatActivity {
    private final String TAG = "HomeActivity";
    public Context mContext = firstActivity.this;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference newsletterReference;
    private ValueEventListener valueEventListener;

    private NewsletterAdapter adapter;
    private SharedPreferences prefs;

    RecyclerView newsletterView;
    TextView parsedHtmlNode;
    String password;
   // Bundle extras = getIntent().getExtras();
    String username;
    TextView head;
    public int ACTIVITY_NUM = 2;
    ScrollView scrollview_news;
    private InterstitialAd mInterstitialAd;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
        head = findViewById(R.id.heading);
        FancyButton data_btn = (FancyButton) findViewById(R.id.data_button);
        parsedHtmlNode = (TextView) findViewById(R.id.welcome5);
        FancyButton aboutbutton = (FancyButton) findViewById(R.id.aboutButton);
        scrollview_news = findViewById(R.id.scrollView_news);

       // FancyButton LaundryButton = (FancyButton) findViewById(R.id.laundryButton);
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/ADAM.CG PRO.otf");
        head.setTypeface(custom_font2);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/SF_Speakeasy.ttf");
        parsedHtmlNode.setTypeface(custom_font);
        parsedHtmlNode.setText("Welcome!");
        newsletterView = (RecyclerView) findViewById(R.id.newsletter_view);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        newsletterView.setLayoutManager(manager);
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
        firebaseDatabase = FirebaseDatabase.getInstance();
        newsletterReference = firebaseDatabase.getReference("/data/newsletter");
        valueEventListener = getValueEventListener();
        newsletterReference.addValueEventListener(valueEventListener);
    }

    private ValueEventListener getValueEventListener(){
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(adapter == null){
                        adapter = new NewsletterAdapter();
                        newsletterView.setAdapter(adapter);
                        newsletterView.setVisibility(View.VISIBLE);
                    }
                    adapter.add(dataSnapshot.getValue(NewsletterData.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,databaseError.getMessage());
            }
        };
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(valueEventListener != null){
            newsletterReference.removeEventListener(valueEventListener);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(valueEventListener == null){
            valueEventListener = getValueEventListener();
        }
        newsletterReference.addValueEventListener(valueEventListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setScrollview_news(){
        TextView heading;
        TextView subtext;
        TextView Superheading = new TextView(this);
        String text = "<font color=#222222>News</font><font color=#B73038>letter</font>";
        Superheading.setText(Html.fromHtml(text));
        Superheading.setPadding(0,10,0,10);
        Superheading.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        Superheading.setTextSize(50);
        Superheading.setTextColor(Color.parseColor("#B73038"));
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/SF_Speakeasy.ttf");
        Superheading.setTypeface(custom_font);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(Superheading);
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


    public class NewsletterData{
        private String heading;
        private String content;

        public NewsletterData(String heading,String content){
            this.heading = heading;
            this.content = content;
        }

        public String getHeading(){
            return heading;
        }

        public String getContent(){
            return content;
        }
    }

}

