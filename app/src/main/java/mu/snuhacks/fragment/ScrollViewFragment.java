package mu.snuhacks.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.florent37.hollyviewpager.HollyViewPagerBus;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

import java.util.ArrayList;

import mu.snuhacks.R;

/**
 * Created by florentchampigny on 07/08/15.
 */
public class ScrollViewFragment extends Fragment {

    private TextView meal1,meal2,meal3;
    private static Context mContext;

    public static ScrollViewFragment newInstance(String title, ArrayList<String> menu_full, Context context){
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putStringArrayList("menu",menu_full);
        ScrollViewFragment fragment = new ScrollViewFragment();
        fragment.setArguments(args);
        mContext = context;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        return inflater.inflate(R.layout.fragment_scroll, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String> Menu_full;

        ObservableScrollView scrollView = getActivity().findViewById(R.id.scrollView);
        TextView title = getActivity().findViewById(R.id.title);

        Menu_full = getArguments().getStringArrayList("menu");
        title.setText("DH2");

        for(int j = 0; j<3; j++){
            String textToHighlight = "Chicken";
            String textToHighlight2 = "Paneer";
            // Construct the formatted text
            String replacedWith = "<b><font color= #13c000>" + textToHighlight + "</font></b>";
            String replacedWith2 = "<b><font color= #13c000>" + textToHighlight2 + "</font></b>";

            // Get the text from TextView
            String originalString = Menu_full.get(j);

            // Replace the specified text/word with formatted text/word
            String modifiedString1 = originalString.replaceAll(textToHighlight,replacedWith);
            String modifiedString = modifiedString1.replaceAll(textToHighlight2,replacedWith2);

            Log.d("Tag",modifiedString);
            Menu_full.set(j,modifiedString);

        }

        FrameLayout cardView1 = getActivity().findViewById(R.id.card1);
        FrameLayout cardView2 = getActivity().findViewById(R.id.card2);
        FrameLayout cardView3 = getActivity().findViewById(R.id.card3);

        meal1 = cardView1.findViewById(R.id.meal);
        meal2 = cardView2.findViewById(R.id.meal);
        meal3 = cardView3.findViewById(R.id.meal);

        TextView menu1 = cardView1.findViewById(R.id.menu);
        TextView menu2 = cardView2.findViewById(R.id.menu);
        TextView menu3 = cardView3.findViewById(R.id.menu);

        Typeface custom_font2 = Typeface.createFromAsset(mContext.getAssets(), "fonts/RegencieLight.ttf");


        meal1.setText("Breakfast");
        meal2.setText("Lunch");
        meal3.setText("Dinner");

        meal1.setTypeface(custom_font2);
        meal2.setTypeface(custom_font2);
        meal3.setTypeface(custom_font2);
        menu1.setTextSize(15);
        menu2.setTextSize(15);
        menu3.setTextSize(15);


        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Junction-regular.otf");
        menu1.setTypeface(custom_font);
        menu2.setTypeface(custom_font);
        menu3.setTypeface(custom_font);

        menu1.setText(Html.fromHtml(Menu_full.get(0)));
        menu2.setText(Html.fromHtml(Menu_full.get(1)));
        menu3.setText(Html.fromHtml(Menu_full.get(2)));

        HollyViewPagerBus.registerScrollView(getActivity(), scrollView);
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e("AAAAA",meal1.getText().toString() + " " + meal2.getText().toString() + " " + meal3.getText().toString());
    }
}
