package ru.kkrukhmalev.profiler;

import org.aspectj.lang.reflect.MethodSignature;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class MethodStatistics {
    private final MethodSignature signature;

    private int invocationCount;

    private long sumExecutionTime;

    public MethodStatistics(MethodSignature signature, int invocationCount, long sumExecutionTime) {
        this.signature = signature;
        this.invocationCount = invocationCount;
        this.sumExecutionTime = sumExecutionTime;
    }

    public int getInvocationCount() {
        return invocationCount;
    }

    public long getSumExecutionTime() {
        return sumExecutionTime;
    }

    public void countExecution(long time) {
        invocationCount++;
        sumExecutionTime += time;
    }

    public double getAverageExecutionTime() {
        return sumExecutionTime / (double) invocationCount;
    }
}
