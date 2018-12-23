package mu.snuhacks;
 /*
<color name="white">#DBFCFF</color>
    <color name="black">#788A8C</color>
    */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import mehdi.sakout.fancybuttons.FancyButton;

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
    TextView head;
    TextView Superheading;
    public int ACTIVITY_NUM = 2;
    ScrollView scrollview_news;
    private InterstitialAd mInterstitialAd;
    FrameLayout card1;
    FrameLayout card2;
    FrameLayout card3;
    FrameLayout card4;
    CardView card_card1;
    CardView card_card2;
    CardView card_card3;
    CardView card_card4;

    LinearLayout data_layout;
    TextView heading1;
    TextView heading2;
    TextView heading3;
    TextView heading4;



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
        head.setPadding(10,0,0,0);
      //  FancyButton data_btn = (FancyButton) findViewById(R.id.data_button);
        parsedHtmlNode = (TextView) findViewById(R.id.welcome5);
        FancyButton aboutbutton = (FancyButton) findViewById(R.id.aboutButton);
        scrollview_news = findViewById(R.id.scrollView_news);
        Typeface custom_font9 = Typeface.createFromAsset(getAssets(), "fonts/Biko_Regular.otf");


        // FancyButton LaundryButton = (FancyButton) findViewById(R.id.laundryButton);
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Archive.otf");
        head.setTypeface(custom_font2);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/ChampagnE.ttf");
        Typeface custom_font7 = Typeface.createFromAsset(getAssets(), "fonts/SF_Speakeasy.ttf");
        parsedHtmlNode.setTypeface(custom_font7);
        String htext = "<font color=#d11141>Welcome!</font>";
        parsedHtmlNode.setTextSize(48);
        parsedHtmlNode.setShadowLayer(3,0,4,Color.parseColor("#8595a1"));
        parsedHtmlNode.setText(Html.fromHtml(htext));

        Superheading = findViewById(R.id.newsletter_text);
        String text = "<font color=#222222>News</font><font color=#d11141>letter</font>";
        Superheading.setShadowLayer(3,0,4,Color.parseColor("#8595a1"));

        Superheading.setText(Html.fromHtml(text));
        Superheading.setTextSize(40);
        Superheading.setTypeface(custom_font7);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        data_layout = findViewById(R.id.data_layout);
        card1 = data_layout.findViewById(R.id.card1);
        card2 = data_layout.findViewById(R.id.card2);
        card3 = data_layout.findViewById(R.id.card3);
        card4 = data_layout.findViewById(R.id.card4);

        card_card1 = card1.findViewById(R.id.card);
        card_card2 = card2.findViewById(R.id.card);
        card_card3 = card3.findViewById(R.id.card);
        card_card4 = card4.findViewById(R.id.card);

        card_card1.setCardBackgroundColor(Color.parseColor("#fff4e6"));
        card_card2.setCardBackgroundColor(Color.parseColor("#fff4e6"));
        card_card3.setCardBackgroundColor(Color.parseColor("#fff4e6"));
        card_card4.setCardBackgroundColor(Color.parseColor("#fff4e6"));


        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        card1.setLayoutParams(new LinearLayout.LayoutParams(width/2,height/9));
        card2.setLayoutParams(new LinearLayout.LayoutParams(width/2,height/9));
        card3.setLayoutParams(new LinearLayout.LayoutParams(width/2,height/9));
        card4.setLayoutParams(new LinearLayout.LayoutParams(width/2,height/9));

        card1.setPadding(20,5,5,5);
        card2.setPadding(5,5,20,5);
        card3.setPadding(20,5,5,5);
        card4.setPadding(5,5,20,5);

        heading1 = card1.findViewById(R.id.meal);
        heading2 = card2.findViewById(R.id.meal);
        heading3 = card3.findViewById(R.id.meal);
        heading4 = card4.findViewById(R.id.meal);

        String xtext = "<font color=#00b159 size =19>Total</font><font size =19 color=#3b5998> attendance</font>";
        heading1.setText(Html.fromHtml(xtext));
        xtext = "<font color=#00b159 size =19>Data</font><font size =19 color=#3b5998> Used</font>";
        heading2.setText(Html.fromHtml(xtext));
        xtext = "<font color=#00b159 size =19>Dh1</font><font size =19 color=#3b5998> Menu</font>";
        heading3.setText(Html.fromHtml(xtext));
        xtext = "<font color=#00b159 size =19>Dh2</font><font size =19 color=#3b5998> Menu</font>";
        heading4.setText(Html.fromHtml(xtext));

        heading1.setShadowLayer(3,0,4,Color.parseColor("#8595a1"));
        heading2.setShadowLayer(3,0,4,Color.parseColor("#8595a1"));
        heading3.setShadowLayer(3,0,4,Color.parseColor("#8595a1"));
        heading4.setShadowLayer(3,0,4,Color.parseColor("#8595a1"));


        int TextSize = 17;

        heading1.setTextSize(TextSize);
        heading2.setTextSize(TextSize);
        heading3.setTextSize(TextSize);
        heading4.setTextSize(TextSize);



        heading1.setTypeface(custom_font9);
        heading2.setTypeface(custom_font9);
        heading3.setTypeface(custom_font9);
        heading4.setTypeface(custom_font9);


        setScrollview_news();
        aboutbutton.setVisibility(View.GONE);
        aboutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startAbout = new Intent(firstActivity.this, About.class);
                startActivity(startAbout);} });


        //     FancyButton attendanceButton = (FancyButton) findViewById(R.id.attendance_button);
    //    FancyButton logoutButton = (FancyButton) findViewById(R.id.logout_button);
     /*   attendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startAttendance = new Intent(firstActivity.this, Attendance.class);
                startActivity(startAttendance);
            }
        }); */
       /* data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startData = new Intent(firstActivity.this, DataUsage.class);
                startActivity(startData);
            }
        }); */
     /*   messMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startData = new Intent(firstActivity.this, messMenu.class);
                startActivity(startData);
            }
        });*/


     /*   LaundryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startAbout = new Intent(firstActivity.this, Laundry.class);
                startActivity(startAbout);
            }
        });
*/
        /*logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });*/

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

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
       // linearLayout.addView(Superheading);
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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_viewbar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,this , bottomNavigationViewEx);
       // bottomNavigationViewEx.setLayoutParams(new RelativeLayout.LayoutParams(width,height/15));

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }



}

