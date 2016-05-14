package hes.cs63.CEPMonitor;

/**
 * Created by sahbi on 5/8/16.
 */
public class StrokeRiskAlarm {

    private String UserID;
    private int Severity;

    public StrokeRiskAlarm(String UserID, int Severity) {
        this.UserID = UserID;
        this.Severity = Severity;
    }

    public StrokeRiskAlarm() {
        this("",0);
    }

    public String getUserID() {
        return UserID;
    }

    public void setRackID(String rackID) {
        this.UserID = UserID;
    }

    public int getSeverity() {
        return Severity;
    }

    public void setSeverity(int Severity) {
        this.Severity = Severity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StrokeRiskAlarm) {
            StrokeRiskAlarm other = (StrokeRiskAlarm) obj;
            return UserID == other.UserID && Severity == other.Severity;
        } else {
            return false;
        }
    }


    @Override
    public String toString() {
        return "Stroke Risk Alert : { Patient : " + getUserID()+ ", Severity : " + getSeverity() + " }";
    }

}
