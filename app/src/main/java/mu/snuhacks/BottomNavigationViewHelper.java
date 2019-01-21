package mu.snuhacks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;


/**
 * Created by User on 5/28/2017.
 */

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";
    private static InterstitialAd mInterstitialAd;

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx, Context context){
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-2461190858191596/4980119936");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);
        bottomNavigationViewEx.setIconSize(21,21);
        bottomNavigationViewEx.setTextSize(12);
        Typeface custom_font9 = Typeface.createFromAsset(context.getAssets(), "fonts/Junction-regular.otf");
        bottomNavigationViewEx.setTypeface(custom_font9);

    }

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_house:
                        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                            mInterstitialAd.setAdListener(new AdListener() {
                                @Override
                                public void onAdClosed() {

                                    Intent intent1 = new Intent(context, MainActivity.class);//ACTIVITY_NUM = 0
                                    context.startActivity(intent1);
                                    callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }
                            });

                        } else {

                            Toast.makeText(context, "Ad did not load", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(context, MainActivity.class);//ACTIVITY_NUM = 0
                            context.startActivity(intent1);
                            callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        }

                        break;

                    case R.id.ic_data_usage:
                        Intent intent2  = new Intent(context, DataUsage.class);//ACTIVITY_NUM = 1
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_mess_menu:
                        Intent intent3 = new Intent(context, messMenu.class);//ACTIVITY_NUM = 2
                        context.startActivity(intent3);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_mark_attendance:
                        Intent intent4 = new Intent(context, AttendanceActivity.class);//ACTIVITY_NUM = 3
                        context.startActivity(intent4);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;


                    case R.id.ic_help:
                        Intent intent5 = new Intent(context, About.class);//ACTIVITY_NUM = 4
                        context.startActivity(intent5);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }


                return false;
            }
        });
    }
}