package mu.snuhacks;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import mehdi.sakout.fancybuttons.FancyButton;


public class About extends AppCompatActivity {
    TextView one;
    TextView two;
    TextView three;
    TextView four;
    TextView about_main;
    ListView List;
    private Context mContext;
    private int ACTIVITY_NUM = 0;
    FancyButton muditbutton;
    FancyButton haributton;

    FancyButton shashvatbutton;
    FancyButton tarinibutton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_test);
        mContext = getApplicationContext();
     /*   one = (TextView) findViewById(R.id.Developer);
        two = (TextView) findViewById(R.id.Mudit);
        three = (TextView) findViewById(R.id.Design);
        four = (TextView) findViewById(R.id.Tarini);
        about_main = (TextView) findViewById(R.id.ABOUT);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/ChiselMark.ttf");
        about_main.setTypeface(custom_font); */
     setupBottomNavigationView();
       muditbutton = findViewById(R.id.mudit_text);
       haributton = findViewById(R.id.hari_text);
       muditbutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(),diagonal_layout_mudit.class);
               getApplicationContext().startActivity(intent);
           }
       });
        haributton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/HaridharRengasamyPhotography/?ref=br_rs\n";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

    }
    private void setupBottomNavigationView() {
        Log.d("tag", "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_viewbar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx,getApplicationContext());
        BottomNavigationViewHelper.enableNavigation(mContext,this , bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
