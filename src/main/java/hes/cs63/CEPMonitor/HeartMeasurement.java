package hes.cs63.CEPMonitor;

/**
 * Created by sahbi on 5/8/16.
 */
public class HeartMeasurement extends Measurement {

    public HeartMeasurement(Measurement m) {
        this.setUserID(m.getUserID());
        this.setValue (m.getValue());
    }

    public int getRisk() {
        int risk = 0;

        risk +=  getValue() <= 50 ? 1 : 0;
        risk +=  getValue() <= 40 ? 1 : 0;

        risk +=  getValue() >= 91 ? 1 : 0;
        risk +=  getValue() >= 110 ? 1 : 0;
        risk +=  getValue() >= 131 ? 1 : 0;

        return risk;
    }

    @Override
    public String toString(){
        return new String("HR   Event : User "+ getUserID() + " , Value : " + getValue());
    }
}
