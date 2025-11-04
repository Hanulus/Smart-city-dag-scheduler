package graph.metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Performance metrics collector.
 * Tracks operation counts and execution time.
 */
public class Metrics {
    private final Map<String, Long> counters = new HashMap<>();
    private long startTime;
    private long endTime;
    
    public void startTimer() {
        startTime = System.nanoTime();
    }
    
    public void stopTimer() {
        endTime = System.nanoTime();
    }
    
    public void increment(String counter) {
        counters.put(counter, counters.getOrDefault(counter, 0L) + 1);
    }
    
    public long get(String counter) {
        return counters.getOrDefault(counter, 0L);
    }
    
    public double getTimeMs() {
        return (endTime - startTime) / 1_000_000.0;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Metrics:\n");
        sb.append(String.format("  Time: %.3f ms\n", getTimeMs()));
        counters.forEach((k, v) -> sb.append(String.format("  %s: %d\n", k, v)));
        return sb.toString();
    }
}
