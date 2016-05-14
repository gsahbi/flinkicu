package hes.cs63.CEPMonitor;

import java.util.Map;

import org.apache.flink.core.fs.FileSystem.WriteMode;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.IngestionTimeExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;

/**
 * Created by sahbi on 5/7/16.
 */
public class CEPMonitor {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        ParameterTool parameterTool = ParameterTool.fromArgs(args);

        // Use ingestion time => TimeCharacteristic == EventTime + IngestionTimeExtractor
        env.enableCheckpointing(1000).
            setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // Input stream of monitoring events
        DataStream<Measurement> messageStream = env
                .addSource(new FlinkKafkaConsumer09<>(
                                    parameterTool.getRequired("topic"),
                                    new MeasurementDeserializer(),
                                    parameterTool.getProperties()))
                .assignTimestampsAndWatermarks(new IngestionTimeExtractor<>());


        DataStream<Measurement> partitionedInput = messageStream.keyBy(
                new KeySelector<Measurement, String>() {
                    @Override
                    public String getKey(Measurement value) throws Exception {
                        return value.getUserID();
                    }
        });


        // Warning pattern: 2 high heart rate events with a high blood pressure within 10 seconds
        Pattern<Measurement, ?> alarmPattern = Pattern.<Measurement>begin("first")
                .subtype(HeartMeasurement.class)
                .where(evt -> evt.getRisk() >= 1)
                .followedBy("middle")
                .subtype(BloodPressureMeasurement.class)
                .where(evt -> evt.getRisk() >= 2)
                .followedBy("last")
                .subtype(HeartMeasurement.class)
                .where(evt -> evt.getRisk() >= 3)
                .within(Time.seconds(10));

        // Create a pattern stream from alarmPattern
        PatternStream<Measurement> patternStream = CEP.pattern(partitionedInput, alarmPattern);


        // Generate risk warnings for each matched alarm pattern
        DataStream<StrokeRiskAlarm> alarms = patternStream.select(new PatternSelectFunction<Measurement, StrokeRiskAlarm>() {
            @Override
            public StrokeRiskAlarm select(Map<String, Measurement> pattern) throws Exception {
                HeartMeasurement first = (HeartMeasurement) pattern.get("first");
                HeartMeasurement last = (HeartMeasurement) pattern.get("last");
                BloodPressureMeasurement middle = (BloodPressureMeasurement) pattern.get("middle");

                return new StrokeRiskAlarm(first.getUserID(), first.getRisk() + last.getRisk() + middle.getRisk());
            }
        });

        alarms.map(v -> v.toString()).writeAsText(parameterTool.getRequired("out"), WriteMode.OVERWRITE);
        messageStream.map(v -> v.toString()).print();

        env.execute("Flink ICU CEP monitoring job");

    }
}
