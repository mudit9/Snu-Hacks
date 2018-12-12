package mu.snuhacks;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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


public class About extends AppCompatActivity {
    TextView one;
    TextView two;
    TextView three;
    TextView four;
    TextView about_main;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        one = (TextView) findViewById(R.id.Developer);
        two = (TextView) findViewById(R.id.Mudit);
        three = (TextView) findViewById(R.id.Design);
        four = (TextView) findViewById(R.id.Tarini);
        about_main = (TextView) findViewById(R.id.ABOUT);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/ChiselMark.ttf");
        about_main.setTypeface(custom_font);


    }
}