package mu.snuhacks;

import java.util.ArrayList;

public class AttendanceResponse {

    private ArrayList<Object> attendanceData;
    private String errorMessage;

    public AttendanceResponse(ArrayList<Object> attendanceData,String errorMessage){
        this.attendanceData = attendanceData;
        this.errorMessage = errorMessage;
    }

    public ArrayList<Object> getAttendanceData(){
        return attendanceData;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}

