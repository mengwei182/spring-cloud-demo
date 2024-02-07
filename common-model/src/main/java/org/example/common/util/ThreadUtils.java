package org.example.common.util;

import java.util.concurrent.*;

/**
 * @author lihui
 * @since 2022/4/8
 */
public class ThreadUtils {
    private static final ExecutorService EXECUTOR_SERVICE;

    static {
        int processors = Runtime.getRuntime().availableProcessors();
        // 核心线程数设置为总核心数的一半
        int corePoolSize = processors / 2 == 0 ? 1 : processors / 2;
        EXECUTOR_SERVICE = new ThreadPoolExecutor(corePoolSize, corePoolSize, Long.MAX_VALUE, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
    }

    private ThreadUtils() {
    }

    public static void execute(Runnable runnable) {
        EXECUTOR_SERVICE.execute(runnable);
    }

    public static Future<?> submit(Runnable runnable) {
        return EXECUTOR_SERVICE.submit(runnable);
    }

    public static Object submitAndGet(Runnable runnable) throws ExecutionException, InterruptedException {
        return EXECUTOR_SERVICE.submit(runnable).get();
    }

    public static <T> T submitAndGet(Runnable runnable, T result) throws ExecutionException, InterruptedException {
        return EXECUTOR_SERVICE.submit(runnable, result).get();
    }

    public static boolean isShutdown() {
        return EXECUTOR_SERVICE.isShutdown();
    }

    public static boolean isTerminated() {
        return EXECUTOR_SERVICE.isTerminated();
    }
}