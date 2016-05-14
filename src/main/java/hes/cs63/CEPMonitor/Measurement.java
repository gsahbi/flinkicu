package hes.cs63.CEPMonitor;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sahbi on 5/8/16.
 */
public  class Measurement {
    //{"UserID": 53, "Type": "BP", "Value": 15}

    @SerializedName("userid")
    private String UserID;

    @SerializedName("type")
    private String Type;

    @SerializedName("value")
    private float Value;

    public String getUserID() {
        return this.UserID;
    }

    public float getValue() {
        return this.Value;
    }

    public String getType() {
        return this.Type;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public void setValue(float Value) {
        this.Value = Value;
    }

    public int getRisk() {

        return 0;
    }
    public String toString(){
        return new String("");
    }

}

