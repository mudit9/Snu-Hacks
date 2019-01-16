package mu.snuhacks;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.florent37.depth.Depth;
import com.github.florent37.depth.DepthProvider;
import com.github.florent37.depth.animations.EnterConfiguration;
import com.github.florent37.depth.animations.ExitConfiguration;
import com.github.florent37.depth.animations.ReduceConfiguration;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private int ACTIVITY_NUM = 0;
    private Toolbar toolbar3;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Depth depth;
    private Context mContext;
    private FrameLayout frame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_layout_test);
        //frame = findViewById(R.id.framelayout);
        depth = DepthProvider.getDepth(this);
 /*      depth
                .animate()
                .reduce(MarkAttendanceF)

                .exit(oldFragment)

                .enter(newFragment)
                .start();
*/
        mContext = this;
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        BottomNavigationView bottomNavigationView;
        tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        depth.setFragmentContainer(R.id.framelayout);
        setupBottomNavigationView();
        changeTabsFont();

    }
    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Junction-regular.otf"));
                    ((TextView) tabViewChild).setTextSize(18);
                }
            }
        }
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


    public void changeFragment(final Fragment oldFragment) {
        final Fragment newFragment = Fragment1.newInstance();
                animateDefault(oldFragment, newFragment);
        }

    private void animateDefault(final Fragment oldFragment, final Fragment newFragment){
        depth
                .animate()
                .reduce(oldFragment)
                .exit(oldFragment)
                .enter(newFragment)
                .start();
    }
    private void animateOnTop(final Fragment oldFragment, final Fragment newFragment){
        depth
                .animate()
                .reduce(oldFragment, new ReduceConfiguration()
                        .setRotationZ(0f)
                        .setRotationX(30f)
                )

                .exit(oldFragment, new ExitConfiguration()
                        .setFinalXPercent(0f)
                        .setFinalYPercent(-1f)
                )
                .enter(newFragment, new EnterConfiguration()
                        .setInitialXPercent(0f)
                        .setInitialYPercent(1f)
                        .setInitialRotationZ(0f)
                        .setInitialRotationX(30f)
                )
                .start();
    }
    public void openResetFragment(final Fragment fragment) {
        depth
                .animate()
                .reduce(fragment, new ReduceConfiguration().setScale(0.5f))
                .revert(fragment)
                .start();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CreditAttendanceF(), "Credit Hours");
        adapter.addFragment(new AttendanceF(), "Check");
        adapter.addFragment(new MarkAttendanceF(), "Mark");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}