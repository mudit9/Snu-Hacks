package mu.snuhacks.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import java.util.ArrayList;

import mu.snuhacks.AttendanceDataCC;
import mu.snuhacks.R;

public class AttendanceAdapterCC extends RecyclerView.Adapter<AttendanceAdapterCC.ViewHolder> {
    private final String TAG = AttendanceAdapter.class.getSimpleName();

    private ArrayList<Object> attendanceData;
    private Context Context;

    private ExpansionLayoutCollection expansionLayoutCollection = new ExpansionLayoutCollection();

    public AttendanceAdapterCC(ArrayList<Object>attendanceData, Context context){
        this.attendanceData = attendanceData;
        this.Context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.expansion_layout_component2,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(attendanceData.get(position));
        Log.d(TAG,"Component inflated");
        expansionLayoutCollection.add(holder.getExpansionLayout());
    }

    @Override
    public int getItemCount() {
        return attendanceData.size();
    }

    public void setAttendanceData(ArrayList<Object> attendanceData){
        Log.e(TAG,attendanceData.toString());
       // this.attendanceData.clear();
       // this.attendanceData.addAll(attendanceData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final String TAG = ViewHolder.class.getSimpleName();

        private ExpansionLayout expansionLayout;
        private TextView courseName;
        private TextView courseAttendance;
        private ListView listView;
        private TextView totalAttendance;

        public ViewHolder(View view){
            super(view);
            expansionLayout = (ExpansionLayout) view.findViewById(R.id.expansion_layout);
            expansionLayoutCollection.openOnlyOne(false);
            // expansionLayout.setEnable(false);
            totalAttendance = view.findViewById(R.id.totalAttendance);
            courseName = (TextView) view.findViewById(R.id.course_name);
            courseAttendance = (TextView) view.findViewById(R.id.course_attendance);
            listView = view.findViewById(R.id.listView);

        }

        public void bind(Object data){
            float attendance = 0.0f;
            Log.d(TAG,"happing");
            ArrayList<String> details_here;
            courseName.setText(((AttendanceDataCC) data).getCourseName());
            DisplayMetrics displayMetrics =Context.getResources().getDisplayMetrics();
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            courseName.setTextSize(height/125);
            try{
                    attendance = Float.parseFloat(((AttendanceDataCC) data).getAttendance());
                } catch(Exception exception){
                    Log.d(TAG,"Exception:- " + exception.getMessage());
                    attendance = 0.0f;
                }
                courseAttendance.setText(((AttendanceDataCC) data).getAttendance());
                details_here = ((AttendanceDataCC) data).getAllDetails();
                Log.d(TAG,details_here.toString());

            courseName.setTypeface(Typeface.createFromAsset(Context.getAssets(),  "fonts/Biko_Regular.otf"));

            if(attendance <75){
                courseAttendance.setTextColor(Color.parseColor("#ff0000"));
                courseAttendance.setTypeface(Typeface.createFromAsset(Context.getAssets(),  "fonts/RegencieLight.ttf"));
            } else{
                courseAttendance.setTextColor(Color.parseColor("#13c000"));
            }


            //ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(Context, R.layout.listview_component,R.id.text41, details_here);
            ListViewAdapter adapter3 = new ListViewAdapter(Context,details_here);
            listView.setAdapter(adapter3);

            expansionLayout.collapse(true);
        }

        public ExpansionLayout getExpansionLayout(){
            return expansionLayout;
        }
    }

}
