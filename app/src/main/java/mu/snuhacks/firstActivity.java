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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.florent37.diagonallayout.DiagonalLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;
import mu.snuhacks.Adapters.NewsletterAdapter;

/**
 * Created by mudit on 12/9/17.
 */

public class firstActivity extends AppCompatActivity {
    private final String TAG = "HomeActivity";
    public Context mContext = firstActivity.this;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference newsletterReference;
    private DatabaseReference superuserReference;
    private ChildEventListener childEventListener;
    private ChildEventListener superuserChildEventListener;

    private RecyclerView newsletterView;
    private NewsletterAdapter adapter;
    private SharedPreferences prefs;

    private ArrayList<String> superUsers = new ArrayList<String>();

    TextView parsedHtmlNode;
    String password;
    String name;
    // Bundle extras = getIntent().getExtras();
    String username;
    // Bundle extras = getIntent().getExtras();
    TextView head;
    TextView Superheading;
    public int ACTIVITY_NUM = 2;
    ScrollView scrollview_news;
    DiagonalLayout diaongal;
    LinearLayout linear_news;
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
    FancyButton logoutButton;
    FancyButton aboutButton;
    TextView heading4;
    TextView menu1;
    TextView menu2;
    TextView last1;
    TextView last2;
    String DataUsageLast;
    String totalAttendance;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = getSharedPreferences("MyPref", 0);
        username = prefs.getString("username","");
        password = prefs.getString("password","");
        name = prefs.getString("name","");
        DataUsageLast = prefs.getString("data_usage","-");
        totalAttendance = prefs.getString("total","-");
        setContentView(R.layout.firstscreen_test);
        //   if (extras != null) {
        //      username = extras.getString("username");
        //       password = extras.getString("password");
        //   }
        setupBottomNavigationView();
      //  mInterstitialAd = new InterstitialAd(this);
      //  mInterstitialAd.setAdUnitId("ca-app-pub-2461190858191596/4980119936");
       // mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //  FancyButton messMenu1 = (FancyButton) findViewById(R.id.messMenuButton);
        head = findViewById(R.id.heading);
        logoutButton = findViewById(R.id.logout_button);
        diaongal = findViewById(R.id.diagonalLayout);

        //  FancyButton data_btn = (FancyButton) findViewById(R.id.data_button);
        parsedHtmlNode = (TextView) findViewById(R.id.welcome5);
        scrollview_news = findViewById(R.id.scrollView_news);
        linear_news = findViewById(R.id.linearlayout_news);
        Typeface custom_font9 = Typeface.createFromAsset(getAssets(), "fonts/Biko_Regular.otf");

        // FancyButton LaundryButton = (FancyButton) findViewById(R.id.laundryButton);
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Junction-regular.otf");
        head.setPadding(10,5,0,0);
        head.setTypeface(custom_font2);
        Typeface custom_font7 = Typeface.createFromAsset(getAssets(), "fonts/Westmeath.ttf");
        Typeface custom_font8= Typeface.createFromAsset(getAssets(), "fonts/Geovana.ttf");

        parsedHtmlNode.setTypeface(custom_font8);
        name = name.split(" ")[0];
        String htext = "<font size = 35 color=#FFFFFF>Welcome </font>" + "<font size=20 color=#FFFFFF>" + name + "!</font>";
        parsedHtmlNode.setTextSize(31);
       // parsedHtmlNode.setShadowLayer(3,0,4,Color.parseColor("#8595a1"));
        parsedHtmlNode.setText(Html.fromHtml(htext));
        parsedHtmlNode.setPadding(0,10,0,0);

        Superheading = findViewById(R.id.newsletter_text);
        String text = "<font color=#FFFFFF>News</font><font color=#d11141>letter</font>";
        Superheading.setShadowLayer(3,0,4,Color.parseColor("#8595a1"));

        Superheading.setText(Html.fromHtml(text));
        Superheading.setTextSize(36);
        Superheading.setTypeface(custom_font7);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        data_layout = findViewById(R.id.data_layout);
        card1 = data_layout.findViewById(R.id.card1);
        card2 = data_layout.findViewById(R.id.card2);
      //  card3 = data_layout.findViewById(R.id.card3);
      //  card4 = data_layout.findViewById(R.id.card4);

        card_card1 = card1.findViewById(R.id.card);
        card_card2 = card2.findViewById(R.id.card);
     //   card_card3 = card3.findViewById(R.id.card);
     //   card_card4 = card4.findViewById(R.id.card);

        card_card1.setCardBackgroundColor(Color.parseColor("#46B1C9"));
        card_card2.setCardBackgroundColor(Color.parseColor("#46B1C9"));

        card_card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(firstActivity.this,AttendanceActivity.class);
                startActivity(intent1);
            }
        });
        card_card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(firstActivity.this,DataUsage.class);
                startActivity(intent2);
            }
        });

   //     card_card3.setCardBackgroundColor(Color.parseColor("#fff4e6"));
     //   card_card4.setCardBackgroundColor(Color.parseColor("#fff4e6"));

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        card1.setLayoutParams(new LinearLayout.LayoutParams(width/2,height/9));
        card2.setLayoutParams(new LinearLayout.LayoutParams(width/2,height/9));
        diaongal.setLayoutParams(new LinearLayout.LayoutParams(width, (height*5)/20));
        Log.d("dimensions","'height - " + height/9 + " width - " + width/9);
      //  card3.setLayoutParams(new LinearLayout.LayoutParams(width/2,height/9));
      //  card4.setLayoutParams(new LinearLayout.LayoutParams(width/2,height/9));

        card1.setPadding(20,5,5,5);
        card2.setPadding(5,5,20,5);
      //  card3.setPadding(20,5,5,5);
      //  card4.setPadding(5,5,20,5);

        heading1 = card1.findViewById(R.id.meal);
        heading2 = card2.findViewById(R.id.meal);
        menu1 = card1.findViewById(R.id.menu);
        menu2 = card2.findViewById(R.id.menu);
        last1 = card1.findViewById(R.id.lastcheckedText);
        last2= card2.findViewById(R.id.lastcheckedText);
        if(!DataUsageLast.equals("-"))
        last2.setVisibility(View.VISIBLE);
        if(!totalAttendance.equals("-"))
        last1.setVisibility(View.VISIBLE);
        menu2.setText(DataUsageLast.substring(0, DataUsageLast.length() - 1).toUpperCase());
        menu1.setText(Html.fromHtml(totalAttendance).toString());
        menu1.setTextColor(Color.parseColor("#f7f7f7"));
        menu2.setTextColor(Color.parseColor("#f7f7f7"));
        String xtext = "<font color=#f7f7f7 size =19>Total</font><font size =19 color=#f7f7f7> attendance</font>";
        heading1.setText(Html.fromHtml(xtext));
        xtext = "<font color=#f7f7f7 size =19>Data</font><font size =19 color=#f7f7f7> Used</font>";
        heading2.setText(Html.fromHtml(xtext));


        int TextSize = 18;

        heading1.setTextSize(TextSize);
        heading2.setTextSize(TextSize);

        heading1.setTypeface(custom_font9);
        heading2.setTypeface(custom_font9);

        setScrollview_news();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        newsletterReference = firebaseDatabase.getReference("/data/newsletter/");
        superuserReference = firebaseDatabase.getReference("/config_data/superuser/");
        childEventListener = getValueEventListener();
        superuserChildEventListener = getSuperUserChildEventListener();
        if(prefs.getString("fcm_key","").length() == 0){
            final DatabaseReference ref = firebaseDatabase.getReference("/data/fcm_tokens/").push();
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("fcm_key",ref.getKey());
                    editor.putString("fcm_token",instanceIdResult.getToken());
                    editor.apply();
                    DatabaseReference reference = firebaseDatabase.getReference("/data/fcm_tokens/" + ref.getKey());
                    reference.setValue(instanceIdResult.getToken()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG,"FCM token stored in DB");
                            }
                        }
                    });
                }
            });
        }
        else{
            if(prefs.getBoolean("name_saved",false)) {
                Log.d(TAG, "FCM token already saved in DB");
                DatabaseReference reference = firebaseDatabase.getReference("/data/fcm/user/" + prefs.getString("fcm_key", ""));
                reference.setValue(username).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("name_saved", true);
                            editor.apply();
                            Log.d(TAG, "Name saved in DB");
                        }
                    }
                });
            }
        }
    }

    private ChildEventListener getSuperUserChildEventListener(){
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()) {
                    Log.e(TAG,dataSnapshot.getValue(String.class));
                    superUsers.add(dataSnapshot.getValue(String.class));
                    if(username.equals(dataSnapshot.getValue(String.class))) {
                        showConfigOption();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    superUsers.remove(dataSnapshot.getValue(String.class));
                    if(username.equals(dataSnapshot.getValue(String.class))){
                        hideConfigOption();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
    }

    private ChildEventListener getValueEventListener(){
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(adapter == null){
                    adapter = new NewsletterAdapter(null, getApplicationContext());
                    newsletterView.setAdapter(adapter);
                }
                NewsletterData data = dataSnapshot.getValue(NewsletterData.class);
                data.setKey(dataSnapshot.getKey());
                adapter.add(data);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                NewsletterData data = dataSnapshot.getValue(NewsletterData.class);
                data.setKey(dataSnapshot.getKey());
                adapter.changeData(data);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                NewsletterData data = dataSnapshot.getValue(NewsletterData.class);
                data.setKey(dataSnapshot.getKey());
                adapter.remove(data);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
        if(childEventListener != null){
            newsletterReference.removeEventListener(childEventListener);
        }
        if(superuserChildEventListener != null){
            superuserReference.removeEventListener(superuserChildEventListener);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(childEventListener == null){
            childEventListener = getValueEventListener();
        }
        if(superuserChildEventListener == null){
            superuserChildEventListener = getSuperUserChildEventListener();
        }
        newsletterReference.addChildEventListener(childEventListener);
        superuserReference.addChildEventListener(superuserChildEventListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setScrollview_news(){

        final TextView heading;
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
        LinearLayout linearLayout = findViewById(R.id.linearlayout_news);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        newsletterView = new RecyclerView(this);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        newsletterView.setLayoutManager(manager);
       // linearLayout.setBackgroundColor(Color.parseColor("#f6cd61"));
        linearLayout.addView(newsletterView);

        if(isSuperuser(username)){
            showConfigOption();
        }
       /* for (int i  = 0; i <8; i++) {
            FrameLayout frameLayout = (FrameLayout) View.inflate(this, R.layout.content_card, null);
            heading = frameLayout.findViewById(R.id.meal);
            subtext = frameLayout.findViewById(R.id.menu);
            heading.setText("heading "+ i);
            subtext.setText("subtext "+ i);
            frameLayout.setPadding(0,5,0,0);
            linearLayout.addView(frameLayout);
        }*/
        //scrollview_news.addView(linearLayout);
    }

    private boolean isSuperuser(String username){
        for(String superUserName : superUsers){
            if(username.equals(superUserName)){
                return true;
            }
        }
        return false;
    }

    private void showConfigOption(){
        LinearLayout linearLayout = findViewById(R.id.linearlayout_news);
        Button configButton = new Button(this);
        configButton.setId(R.id.config_button);
        configButton.setText("Change data");
        linearLayout.addView(configButton);
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent configIntent = new Intent(getApplicationContext(),add_data_activity.class);
                startActivity(configIntent);
            }
        });
    }

    private void hideConfigOption(){
        LinearLayout linearLayout = findViewById(R.id.linearlayout_news);
        linearLayout.removeView((Button) findViewById(R.id.config_button));
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
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx, getApplicationContext());
        BottomNavigationViewHelper.enableNavigation(mContext,this , bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    public static class NewsletterData{
        private String heading;
        private String content;
        private String link;
        private String key;

        public NewsletterData(){
        }

        public NewsletterData(String heading,String content,String link){
            this.heading = heading;
            this.content = content;
            this.link = link;
        }

        public String getHeading(){
            return heading;
        }

        public String getContent(){
            return content;
        }

        public String getLink(){
            return link;
        }

        public String getKey(){
            return key;
        }

        public void setHeading(String heading){
            this.heading = heading;
        }

        public void setLink(String link){
            this.link = link;
        }

        public void setContent(String content){
            this.content = content;
        }

        public void setKey(String key){
            this.key = key;
        }
    }

}
