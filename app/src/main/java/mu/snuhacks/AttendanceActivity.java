package mu.snuhacks;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.florent37.depth.Depth;
import com.github.florent37.depth.DepthProvider;
import com.github.florent37.depth.animations.ReduceConfiguration;

import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private Toolbar toolbar3;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    final Depth depth = DepthProvider.getDepth(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_layout_test);
 /*      depth
                .animate()
                .reduce(MarkAttendanceF)

                .exit(oldFragment)

                .enter(newFragment)
                .start();
*/
        toolbar3 = findViewById(R.id.toolbar_here);
        setSupportActionBar(toolbar3);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //depth.setFragmentContainer(R.id.fragment_container);

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

    public void openResetFragment(final Fragment fragment) {
        depth
                .animate()
                .reduce(fragment, new ReduceConfiguration().setScale(0.5f))
                .revert(fragment)
                .start();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AttendanceF(), "Check");
        adapter.addFragment(new AttendanceF(), "Credit Hours");
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