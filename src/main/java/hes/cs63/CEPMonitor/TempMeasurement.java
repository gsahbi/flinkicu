package hes.cs63.CEPMonitor;

/**
 * Created by sahbi on 5/8/16.
 */
public class TempMeasurement extends Measurement {
    public TempMeasurement(Measurement m) {
        this.setUserID(m.getUserID());
        this.setValue (m.getValue());
    }
    public int getRisk() {
        int risk = 0;

        risk +=  getValue() <= 36 ? 1 : 0;
        risk +=  getValue() <= 35 ? 2 : 0;

        risk +=  getValue() >= 38.1 ? 1 : 0;
        risk +=  getValue() >= 39.1 ? 1 : 0;

        return risk;
    }
    @Override
    public String toString(){
        return new String("TEMP Event : User "+ getUserID() + " , Value : " + getValue());
    }
}
