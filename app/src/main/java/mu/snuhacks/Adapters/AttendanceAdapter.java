package mu.snuhacks.Adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import java.util.ArrayList;

import mu.snuhacks.AttendanceData;
import mu.snuhacks.R;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private final String TAG = AttendanceAdapter.class.getSimpleName();

    private ArrayList<AttendanceData> attendanceData;

    private ExpansionLayoutCollection expansionLayoutCollection = new ExpansionLayoutCollection();

    public AttendanceAdapter(ArrayList<AttendanceData>attendanceData){
        this.attendanceData = attendanceData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.expansion_layout_component,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(attendanceData.get(position));
        expansionLayoutCollection.add(holder.getExpansionLayout());
    }

    @Override
    public int getItemCount() {
        return attendanceData.size();
    }

    public void setAttendanceData(ArrayList<AttendanceData> attendanceData){
        this.attendanceData = attendanceData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final String TAG = ViewHolder.class.getSimpleName();

        private ExpansionLayout expansionLayout;
        private TextView courseName;
        private TextView courseAttendance;

        public ViewHolder(View view){
            super(view);
            expansionLayout = (ExpansionLayout) view.findViewById(R.id.expansion_layout);
            courseName = (TextView) view.findViewById(R.id.course_name);
            courseAttendance = (TextView) view.findViewById(R.id.course_attendance);
        }

        public void bind(AttendanceData data){
            courseName.setText(data.getCourseName());
            float attendance = 0.0f;
            try{
                attendance = Float.parseFloat(data.getCourseAttendance());
            } catch(Exception exception){
                Log.d(TAG,"Exception:- " + exception.getMessage());
            }
            if(attendance > 75){
                 courseAttendance.setTextColor(Color.parseColor("#ff0000"));
            } else{
                courseAttendance.setTextColor(Color.parseColor("#13c000"));
            }
            courseAttendance.setText(data.getCourseAttendance());
            expansionLayout.collapse(false);
        }

        public ExpansionLayout getExpansionLayout(){
            return expansionLayout;
        }
    }

}
