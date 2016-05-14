package hes.cs63.CEPMonitor;

/**
 * Created by sahbi on 5/8/16.
 */
public class BloodPressureMeasurement extends Measurement {

    public BloodPressureMeasurement(Measurement m) {
        this.setUserID(m.getUserID());
        this.setValue (m.getValue());
    }

    public int getRisk() {
        int risk = 0;

        risk +=  getValue() <= 110 ? 1 : 0;
        risk +=  getValue() <= 100 ? 1 : 0;
        risk +=  getValue() <= 90 ? 1 : 0;

        risk +=  getValue() >= 220 ? 3 : 0;

        return risk;
    }
    @Override
    public String toString(){
        return new String("BP   Event : User "+ getUserID() + " , Value : " + getValue());
    }
}
