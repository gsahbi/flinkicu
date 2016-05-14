package hes.cs63.CEPMonitor;

import com.google.gson.Gson;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;
import static org.apache.flink.api.java.typeutils.TypeExtractor.getForClass;

import java.io.IOException;

/**
 * Created by sahbi on 5/8/16.
 */
 public class MeasurementDeserializer implements KeyedDeserializationSchema<Measurement> {
    private Gson gson;
    @Override
    public Measurement deserialize(byte[] messageKey,
                                   byte[] message,
                                   String topic,
                                   int partition,
                                   long offset) throws IOException {
        if (gson == null) {
            gson = new Gson();
        }
        Measurement m = gson.fromJson(new String(message), Measurement.class);
        if (m.getType().equals("HR")) {
            return new HeartMeasurement(m);
        } else if (m.getType().equals("TEMP")) {
            return new TempMeasurement(m);
        }  else if (m.getType().equals("SBP")) {
            return new BloodPressureMeasurement(m);
        } else return m;
    }

    @Override
    public boolean isEndOfStream(Measurement nextElement) {
        return false;
    }

    @Override
    public TypeInformation<Measurement> getProducedType() {
        return getForClass(Measurement.class);
    }
}