package com.example.demo.service;

import lombok.*;

/**
 * PerformanceMetrics - Data class to store performance comparison results
 * Used to demonstrate the impact of N+1 problem and its solution
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceMetrics {
    private long naiveQueryTime;      // Time for N+1 queries
    private long optimizedQueryTime;  // Time for optimized JOIN FETCH
    private int recordsCount;
    private long timeSaved;
    private double improvementPercentage;
    
    public void calculateImprovement() {
        timeSaved = naiveQueryTime - optimizedQueryTime;
        if (naiveQueryTime > 0) {
            improvementPercentage = ((double) timeSaved / naiveQueryTime) * 100;
        }
    }
}
