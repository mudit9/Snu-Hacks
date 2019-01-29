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


import mu.snuhacks.AttendanceData;
import mu.snuhacks.AttendanceDataCC;
import mu.snuhacks.R;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private final String TAG = AttendanceAdapter.class.getSimpleName();

    private ArrayList<Object> attendanceData;
    private Context Context;

    private ExpansionLayoutCollection expansionLayoutCollection = new ExpansionLayoutCollection();

    public AttendanceAdapter(ArrayList<Object>attendanceData, Context context){
        this.attendanceData = attendanceData;
        this.Context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.expansion_layout_component,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(attendanceData.get(position));
//        Log.e(TAG,((AttendanceDataCC) attendanceData.get(position)).getAttendance().trim().substring(0,((AttendanceDataCC) attendanceData.get(position)).getAttendance().indexOf('%')));
        expansionLayoutCollection.add(holder.getExpansionLayout());
    }

    @Override
    public int getItemCount() {
        if(attendanceData != null) {
            return attendanceData.size();
        }
        return 0;
    }

    public void setAttendanceData(ArrayList<Object> attendanceData){
        this.attendanceData = attendanceData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final String TAG = ViewHolder.class.getSimpleName();

        private ExpansionLayout expansionLayout;
        private TextView courseName;
        private TextView courseAttendance;
        private ListView listView;
        private TextView text;

        public ViewHolder(View view){
            super(view);
            expansionLayout = (ExpansionLayout) view.findViewById(R.id.expansion_layout);
            expansionLayoutCollection.openOnlyOne(false);
           // expansionLayout.setEnable(false);
            courseName = (TextView) view.findViewById(R.id.course_name);
            courseAttendance = (TextView) view.findViewById(R.id.course_attendance);
            listView = view.findViewById(R.id.listView);

        }

        public void bind(Object data){
            float attendance = 0.0f;
            Log.d(TAG,"happing1");
            ArrayList<String> details_here;
            if(data instanceof AttendanceData){
                courseName.setText(((AttendanceData) data).getCourseName());
                DisplayMetrics displayMetrics =Context.getResources().getDisplayMetrics();
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                courseName.setTextSize(height/125);
                try{
                    attendance = Float.parseFloat(((AttendanceData) data).getCourseAttendance());
                } catch(Exception exception){
                    Log.d(TAG,"Exception:- " + exception.getMessage());
                    attendance = 0.0f;
                }
                courseAttendance.setText(((AttendanceData) data).getCourseAttendance());
                details_here = ((AttendanceData) data).getAllDetails();
            } else{
                courseName.setText(((AttendanceDataCC) data).getCourseName());
                try{
                    String subs = ((AttendanceDataCC) data).getAttendance().trim().substring(0,((AttendanceDataCC) data).getAttendance().indexOf('%'));
                    attendance = Float.parseFloat(subs);
                } catch(Exception exception){
                    Log.d(TAG,"Exception:- " + exception.getMessage());
                    attendance = 0.0f;
                }
                courseAttendance.setText(((AttendanceDataCC) data).getAttendance());
                details_here = ((AttendanceDataCC) data).getAllDetails();
            }
            courseName.setTypeface(Typeface.createFromAsset(Context.getAssets(),  "fonts/Biko_Regular.otf"));

            if(attendance <75){
                 courseAttendance.setTextColor(Color.parseColor("#ff0000"));
                 courseAttendance.setTypeface(Typeface.createFromAsset(Context.getAssets(),  "fonts/RegencieLight.ttf"));
            } else{
                courseAttendance.setTextColor(Color.parseColor("#13c000"));
            }


          //  ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(Context, R.layout.listview_component,R.id.text41, details_here);
            ListViewAdapter adapter3 = new ListViewAdapter(Context,details_here);
            listView.setAdapter(adapter3);

            expansionLayout.collapse(true);
        }

        public ExpansionLayout getExpansionLayout(){
            return expansionLayout;
        }
    }

}
