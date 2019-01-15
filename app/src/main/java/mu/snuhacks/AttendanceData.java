package mu.snuhacks;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AttendanceData implements Parcelable {
    private String courseName;
    private String courseCode;
    private String courseAttendance;
    private String enRollmentDate;
    private String classesConducted;
    private String classesAttended;
    private String officialLeave;

    private AttendanceData(Parcel in){
        courseName = in.readString();
        courseCode = in.readString();
        courseAttendance = in.readString();
        enRollmentDate = in.readString();
        classesAttended = in.readString();
        classesConducted = in.readString();
        officialLeave = in.readString();
    }

    public AttendanceData(String courseName,String courseCode,String enRollmentDate,String classesConducted,String classesAttended,String officialLeave,String courseAttendance){

        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseAttendance = courseAttendance;
        this.enRollmentDate = enRollmentDate;
        this.classesConducted = classesConducted;
        this.classesAttended = classesAttended;
        this.officialLeave = officialLeave;

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
        out.writeString(enRollmentDate);
        out.writeString(classesAttended);
        out.writeString(classesConducted);
        out.writeString(officialLeave);
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

    public String getEnRollmentDate(){
        return enRollmentDate;
    }


    public void setCourseName(String courseName){
        this.courseName = courseName;
    }

    public void setCourseCode(String courseCode){
        this.courseCode = courseCode;
    }


    public ArrayList getAllDetails() {
        ArrayList<String> details = new ArrayList<>();
        details.add("Course code \t-\t " + courseCode);
        details.add("Course name \t-\t " + courseName);
        details.add("Enrollment Date \t-\t " + enRollmentDate);
        details.add("Classes Attended \t-\t " + classesAttended);
        details.add("Classes Conducted  \t-\t " + classesConducted);
        details.add("Official Leave \t-\t " + officialLeave);
        details.add("Course attendance \t-\t " + courseAttendance);
        return details;
    }

    public void setCourseAttendance(String courseAttendance){
        this.courseAttendance = courseAttendance;
    }

    public void setEnRollmentDate(String courseAttendanceCC){
        this.enRollmentDate = enRollmentDate;
    }

}
