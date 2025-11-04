package graph.metrics;

/**
 * Interface for collecting performance metrics during graph algorithm execution.
 * Tracks operation counts and execution time.
 */
public interface Metrics {
    
    /**
     * Records the start time of algorithm execution.
     */
    void startTimer();
    
    /**
     * Records the end time of algorithm execution.
     */
    void stopTimer();
    
    /**
     * Returns the elapsed time in nanoseconds.
     */
    long getElapsedTimeNanos();
    
    /**
     * Returns the elapsed time in milliseconds.
     */
    double getElapsedTimeMillis();
    
    /**
     * Increments a counter by the given name.
     * 
     * @param counterName name of the counter (e.g., "dfs_visits", "edge_relaxations")
     */
    void incrementCounter(String counterName);
    
    /**
     * Increments a counter by a specific amount.
     * 
     * @param counterName name of the counter
     * @param amount amount to increment
     */
    void incrementCounter(String counterName, long amount);
    
    /**
     * Gets the current value of a counter.
     * 
     * @param counterName name of the counter
     * @return counter value, or 0 if not found
     */
    long getCounter(String counterName);
    
    /**
     * Resets all metrics (counters and timer).
     */
    void reset();
    
    /**
     * Returns a string representation of all metrics.
     */
    String getSummary();
}
