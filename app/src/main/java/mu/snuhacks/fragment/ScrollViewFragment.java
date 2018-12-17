package mu.snuhacks.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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


    public static ScrollViewFragment newInstance(String title, ArrayList<String> menu_full){
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putStringArrayList("menu",menu_full);
        ScrollViewFragment fragment = new ScrollViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
            String replacedWith2 = "<b><font color= #13c000>" + textToHighlight + "</font></b>";

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

        TextView meal1 = cardView1.findViewById(R.id.meal);
        TextView meal2 = cardView2.findViewById(R.id.meal);
        TextView meal3 = cardView3.findViewById(R.id.meal);

        TextView menu1 = cardView1.findViewById(R.id.menu);
        TextView menu2 = cardView2.findViewById(R.id.menu);
        TextView menu3 = cardView3.findViewById(R.id.menu);


        meal1.setText("Breakfast");
        meal2.setText("Lunch");
        meal3.setText("Dinner");
        menu1.setText(Html.fromHtml(Menu_full.get(0)));
        menu2.setText(Html.fromHtml(Menu_full.get(1)));
        menu3.setText(Html.fromHtml(Menu_full.get(2)));




        HollyViewPagerBus.registerScrollView(getActivity(), scrollView);
    }
}
