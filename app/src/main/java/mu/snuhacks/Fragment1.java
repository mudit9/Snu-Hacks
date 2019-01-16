package mu.snuhacks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.florent37.depth.Depth;
import com.github.florent37.depth.DepthProvider;


public class Fragment1 extends Fragment {
    private Depth depth;
    private TextView text;

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
//        text = getView().findViewById(R.id.empty_text_view1);
        this.depth = DepthProvider.getDepth(getContext());
        View view = depth.setupFragment(10f, 10f, inflater.inflate(R.layout.mark_attendance_f, container, false));
        Log.d("tag",view.toString());
     //   text.setText("Attendance Marked Succesfully!");
       // Toasty.success(getContext(), "Marked!", Toast.LENGTH_SHORT, true).show();

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