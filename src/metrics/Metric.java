package metrics;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Metric {

    private static final String TAG = Metric.class.getSimpleName();

    private static Map<MetricKey, Long> metrics =  new EnumMap<>(MetricKey.class);

    public static void start(MetricKey metricKey) {
        metrics.put(metricKey, System.currentTimeMillis());
    }

    public static void record(MetricKey metricKey) {
        if (!metrics.containsKey(metricKey)) {
            Logger.getLogger(TAG).log(Level.WARNING, "Metric " + metricKey.name() + " not started.");
            return;
        }

        long difference = System.currentTimeMillis() - metrics.get(metricKey);
        Logger.getLogger(TAG).log(Level.INFO, metricKey.name() + ": " + (double) difference / 1000 + " seconds");
    }
}
