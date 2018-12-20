package mu.snuhacks;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.florent37.hollyviewpager.HollyViewPager;
import com.github.florent37.hollyviewpager.HollyViewPagerConfigurator;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mu.snuhacks.fragment.RecyclerViewFragment;
import mu.snuhacks.fragment.ScrollViewFragment;

public class messMenu extends AppCompatActivity {
    private Context mContext;
    int pageCount = 2;
    AVLoadingIndicatorView avi3;
    TextView head;
    public ArrayList<String> dh1_menu; //WILL GO IN RECYCLER FRAGMENT
    public ArrayList<String> dh2_menu; //WILL GO IN SCROLLVIEW FRAGMENT
    public HollyViewPager hollyViewPager;
    ProgressBar mProgressbar;
    public int ACTIVITY_NUM=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mess_menu);
        hollyViewPager = findViewById(R.id.hollyViewPager);
        mContext = this;
        setupBottomNavigationView();
        head = findViewById(R.id.heading);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setTitleTextColor(0xFFFFFFFF);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressbar = findViewById(R.id.ProgressBar);
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/ADAM.CG PRO.otf");
        head.setTypeface(custom_font2);
        avi3= (AVLoadingIndicatorView) findViewById(R.id.avielement3);
        JsoupAsyncTask2 jsoupAsyncTask2 = new JsoupAsyncTask2();
        jsoupAsyncTask2.execute();



        }

    private void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_viewbar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,this , bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private class JsoupAsyncTask2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressbar.setIndeterminate(true);
            avi3.smoothToShow();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String breakfast1="";
                String lunch1="";
                String dinner1="";
                String breakfast2= "";
                String lunch2="";
                String dinner2="";
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
                Connection.Response response = Jsoup.connect("http://messmenu.snu.in/messMenu.php")
                        .method(Connection.Method.POST)
                        .execute();
                try {
                    Document document2 = response.parse();
                    Element elementsByTag = document2.getElementsByClass("table table-striped table-bordered table-hover").get(1);
                    Element elementsByTags = elementsByTag.getElementsByTag("tbody").get(0);
                    Element elementsByTags1 = elementsByTags.getElementsByTag("tr").get(0);
                    Elements elementByTags2 = elementsByTags1.getElementsByTag("td");
                    try{
                    breakfast2 = elementByTags2.get(1).text();
                    lunch2 = elementByTags2.get(2).text();
                    dinner2 = elementByTags2.get(3).text(); }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        breakfast2 = elementByTags2.get(0).text();
                        lunch2 = elementByTags2.get(0).text();
                        dinner2 = elementByTags2.get(0).text();
                    }

                    Element elementsByTag1 = document2.getElementsByClass("table table-striped table-bordered table-hover").get(0);
                    Element elementsByTags2 = elementsByTag1.getElementsByTag("tbody").get(0);
                    Element elementsByTags3 = elementsByTags2.getElementsByTag("tr").get(0);
                    Elements elementByTags4 = elementsByTags3.getElementsByTag("td");
                    try{
                        breakfast2 = elementByTags4.get(1).text();
                        lunch2 = elementByTags4.get(2).text();
                        dinner2 = elementByTags4.get(3).text(); }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        breakfast1 = elementByTags4.get(0).text();
                        lunch1 = elementByTags4.get(0).text();
                        dinner1 = elementByTags4.get(0).text();
                    }

                    dh1_menu = new ArrayList<>(Arrays.asList(breakfast1, lunch1, dinner1));

                    dh2_menu = new ArrayList<>(Arrays.asList(breakfast2, lunch2, dinner2));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            avi3.smoothToHide();
            mProgressbar.setIndeterminate(false);
            mProgressbar.setVisibility(View.GONE);
            hollyViewPager.getViewPager().setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
            hollyViewPager.setConfigurator(new HollyViewPagerConfigurator() {
                @Override
                public float getHeightPercentForPage(int page) {
                    return ((page+4)%10)/10f;
                }
            });

            hollyViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    if(position%2==0) {
                        Fragment fragment = new RecyclerViewFragment();
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("dh1_menu", dh1_menu);
                        fragment.setArguments(bundle);
                        return fragment;
                    }
                    else
                        return ScrollViewFragment.newInstance((String) getPageTitle(position),dh2_menu);
                }

                @Override
                public int getCount() {
                    return pageCount;}
                @Override
                public CharSequence getPageTitle(int position) {
                    if (position == 1)
                        return "DH2";
                    else
                        return "DH1";
                }
            });


        }
    }
    }
