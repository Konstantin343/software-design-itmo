package ru.kkrukhmalev.profiler;

import org.aspectj.lang.reflect.MethodSignature;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Profiler {
    private String packageName;

    private static Profiler instance = null;

    private final Map<String, MethodStatisticsTree> statistics = new HashMap<>();

    private Profiler() {
    }

    public static Profiler getInstance() {
        if (instance == null) {
            instance = new Profiler();
        }
        return instance;
    }
    
    public static String use(String packageName, Runnable action) {
        Profiler profiler = Profiler.getInstance();
        profiler.setPackageName(packageName);
        String result;
        try {
            action.run();
        } finally {
            result = profiler.collectResultsToString();
        }
        return result;
    }

    public void setPackageName(String packageName) {
        instance.packageName = packageName;
        if (!instance.statistics.containsKey(packageName)) {
            instance.statistics.put(packageName, new MethodStatisticsTree(packageName));
        }
    }

    public void drop() {
        statistics.clear();
    }

    public void process(MethodSignature signature, long time) {
        if (!signature.getDeclaringTypeName().startsWith(packageName)) {
            return;
        }

        statistics.get(packageName).updateStatic(signature, time);
    }

    public String collectResultsToString() {
        StringBuilder result = new StringBuilder(
                Colors.ANSI_RED + "\u001B[31m=== Profiling results ===" + Colors.ANSI_RESET + "\n"
        );
        for (MethodStatisticsTree tree : statistics.values()) {
            tree.collectToString(result);
        }
        return result.toString();
    }
}
