package mu.snuhacks.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.hollyviewpager.HollyViewPager;
import com.github.florent37.hollyviewpager.HollyViewPagerBus;

import java.util.ArrayList;

import mu.snuhacks.R;
import mu.snuhacks.recycler.RecyclerAdapter;

/**
 * Created by florentchampigny on 07/08/15.
 */
public class RecyclerViewFragment extends Fragment {
    ArrayList<String> menu;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerView);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            menu = bundle.getStringArrayList("dh1_menu");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(new RecyclerAdapter(menu));

        HollyViewPagerBus.registerRecyclerView(getActivity(), recyclerView);
    }
}
