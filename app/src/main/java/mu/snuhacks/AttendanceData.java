package mu.snuhacks;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AttendanceData implements Parcelable {
    private String courseName;
    private String courseCode;
    private String courseAttendance;

    private AttendanceData(Parcel in){
        courseName = in.readString();
        courseCode = in.readString();
        courseAttendance = in.readString();
    }

    public AttendanceData(String courseName,String courseCode,String courseAttendance){
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseAttendance = courseAttendance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final AttendanceData.ClassLoaderCreator<AttendanceData> CREATOR = new Parcelable.ClassLoaderCreator<AttendanceData>(){
        @Override
        public AttendanceData createFromParcel(Parcel in, ClassLoader loader) {
            return new AttendanceData(in);
        }

        @Override
        public AttendanceData createFromParcel(Parcel in){
            return new AttendanceData(in);
        }

        @Override
        public AttendanceData[] newArray(int size){
            return new AttendanceData[size];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(courseName);
        out.writeString(courseCode);
        out.writeString(courseAttendance);
    }

    public String getCourseName(){
        return courseName;
    }

    public String getCourseCode(){
        return courseCode;
    }

    public String getCourseAttendance(){
        return courseAttendance;
    }

    public void setCourseName(String courseName){
        this.courseName = courseName;
    }

    public void setCourseCode(String courseCode){
        this.courseCode = courseCode;
    }

    public ArrayList getAllDetails(){
        ArrayList<String> details = new ArrayList<>();
        details.add(courseCode);
        details.add(courseAttendance);
        details.add(courseName);
        return details;
    }

    public void setCourseAttendance(String courseAttendance){
        this.courseAttendance = courseAttendance;
    }

}
