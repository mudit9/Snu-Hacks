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
    private Depth depth;

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
        this.depth = DepthProvider.getDepth(getContext());
        View view = depth.setupFragment(10f, 10f, inflater.inflate(R.layout.mark_attendance_f, container, false));
        Log.d("tag",view.toString());
     //   this.depth = DepthProvider.getDepth(getActivity().getApplicationContext());
     //   depth.onFragmentReady(this);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        Log.d("tag","fragment1 created here 2");

        super.onViewCreated(view, savedInstanceState);
        final Depth depth = DepthProvider.getDepth(view.getContext());
        depth.onFragmentReady(this);
    //    final Depth depth = DepthProvider.getDepth(view.getContext());
     //   depth.onFragmentReady(this);
    }

}