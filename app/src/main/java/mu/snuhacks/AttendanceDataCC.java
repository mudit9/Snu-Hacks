package mu.snuhacks;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AttendanceDataCC implements Parcelable {

    private String courseName;
    private String courseCode;
    private String credits;
    private String lecAtt;
    private String pracAtt;
    private String tutAtt;
    private String attendance;

    public AttendanceDataCC(String courseName,String courseCode,String credits,String lecAtt,String tutAtt,String pracAtt,String attendance){
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
        this.lecAtt = lecAtt;
        this.pracAtt = pracAtt;
        this.tutAtt = tutAtt;
        this.attendance = attendance;
    }

    private AttendanceDataCC(Parcel in){
        courseName = in.readString();
        courseCode = in.readString();
        credits = in.readString();
        lecAtt = in.readString();
        pracAtt = in.readString();
        tutAtt = in.readString();
        attendance = in.readString();
    }

    public static final AttendanceDataCC.ClassLoaderCreator<AttendanceDataCC> CREATOR = new Parcelable.ClassLoaderCreator<AttendanceDataCC>(){
        @Override
        public AttendanceDataCC createFromParcel(Parcel in, ClassLoader loader) {
            return new AttendanceDataCC(in);
        }

        @Override
        public AttendanceDataCC createFromParcel(Parcel in){
            return new AttendanceDataCC(in);
        }

        @Override
        public AttendanceDataCC[] newArray(int size){
            return new AttendanceDataCC[size];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(courseName);
        out.writeString(courseCode);
        out.writeString(credits);
        out.writeString(lecAtt);
        out.writeString(pracAtt);
        out.writeString(tutAtt);
        out.writeString(attendance);
    }

    public ArrayList getAllDetails(){
        ArrayList<String> details = new ArrayList<>();
        details.add("Course Code - " + courseCode);
        details.add("Course Name - " +courseName);
        details.add("Lecture Attendance - " +lecAtt);
        details.add("Practical Attendance - " +pracAtt);
        details.add("Tutorial Attendance - " +tutAtt);
        details.add("Course Credits - " +credits);
        details.add("Course Attendance - " + attendance);
        return details;
    }

    public String getCourseName(){
        return courseName;
    }

    public String getCourseCode(){
        return courseCode;
    }

    public String getCredits(){
        return credits;
    }

    public String getLecAtt(){
        return lecAtt;
    }

    public String getPracAtt(){
        return pracAtt;
    }

    public String getTutAtt(){
        return tutAtt;
    }

    public String getAttendance(){
        return attendance;
    }

    public void setCourseName(String courseName){
        this.courseName = courseName;
    }

    public void setCourseCode(String courseCode){
        this.courseCode = courseCode;
    }

    public void setCredits(String credits){
        this.credits = credits;
    }

    public void setLecAtt(String lecAtt){
        this.lecAtt = lecAtt;
    }

    public void setPracAtt(String pracAtt){
        this.pracAtt = pracAtt;
    }

    public void setTutAtt(String tutAtt){
        this.tutAtt = tutAtt;
    }

    public void setAttendance(String attendance){
        this.attendance = attendance;
    }

}
