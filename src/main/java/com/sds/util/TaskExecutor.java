package com.sds.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Day 31: Multithreading
public class TaskExecutor {
    
    // Thread pool to handle background tasks like notifications or file generation
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    public static void submitTask(Runnable task) {
        executor.submit(task);
    }

    public static void shutdown() {
        executor.shutdown();
    }
}
