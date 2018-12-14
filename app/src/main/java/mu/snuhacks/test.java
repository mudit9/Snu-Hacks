package mu.snuhacks;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.github.florent37.hollyviewpager.HollyViewPager;
import com.github.florent37.hollyviewpager.HollyViewPagerConfigurator;

import mu.snuhacks.fragment.RecyclerViewFragment;
import mu.snuhacks.fragment.ScrollViewFragment;

public class test extends AppCompatActivity {

    int pageCount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        HollyViewPager hollyViewPager = findViewById(R.id.hollyViewPager);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                if(position%2==0)
                    return new RecyclerViewFragment();
                else
                return ScrollViewFragment.newInstance((String) getPageTitle(position));
            }

            @Override
            public int getCount() {
                return pageCount;}
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 1)
                    return "DH1";
                 else
                    return "DH2";
                }
              });



        }
    }