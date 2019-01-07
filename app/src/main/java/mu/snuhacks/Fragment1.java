package mu.snuhacks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.depth.Depth;
import com.github.florent37.depth.DepthProvider;

public class Fragment1 extends Fragment {

    public static Fragment newInstance() {
        Log.d("tag","fragment1 created");
        final Bundle args = new Bundle();
        final Fragment waterFragment = new Fragment1();
        waterFragment.setArguments(args);
        return waterFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("tag","fragment1 created here ");

        return inflater.inflate(R.layout.depth_fragment_1, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        Log.d("tag","fragment1 created here 2");

        super.onViewCreated(view, savedInstanceState);
        final Depth depth = DepthProvider.getDepth(view.getContext());
        depth.onFragmentReady(this);
    }

}