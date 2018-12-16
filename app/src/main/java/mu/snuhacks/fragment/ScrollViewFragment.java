package mu.snuhacks.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.florent37.hollyviewpager.HollyViewPagerBus;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

import mu.snuhacks.R;

/**
 * Created by florentchampigny on 07/08/15.
 */
public class ScrollViewFragment extends Fragment {

    private TextView meal1,meal2,meal3;



    public static ScrollViewFragment newInstance(String title){
        Bundle args = new Bundle();
        args.putString("title",title);
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
        ObservableScrollView scrollView = view.findViewById(R.id.scrollView);
        TextView title = getActivity().findViewById(R.id.title);

///        if(getArguments().getString("title").equals("TITLE 1") )
   //         title.setText("DH1");
     //   else
            title.setText("DH2");


        FrameLayout cardView1 = getActivity().findViewById(R.id.card1);
        FrameLayout cardView2 = getActivity().findViewById(R.id.card2);
        FrameLayout cardView3 = getActivity().findViewById(R.id.card3);

        meal1 = cardView1.findViewById(R.id.meal);
        meal2 = cardView2.findViewById(R.id.meal);
        meal3 = cardView3.findViewById(R.id.meal);

        meal1.setText("Breakfast");
        meal2.setText("Lunch");
        meal3.setText("Dinner");



        HollyViewPagerBus.registerScrollView(getActivity(), scrollView);
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e("AAAAA",meal1.getText().toString() + " " + meal2.getText().toString() + " " + meal3.getText().toString());
    }
}
