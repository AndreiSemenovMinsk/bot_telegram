package ru.skidoz.service;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author andrey.semenov
 */
public class MyThreadPoolExecutor extends ThreadPoolExecutor {
//    private final Logger logger = LoggerConfigurator.initTraceSyslog(getClass().getName());

    public MyThreadPoolExecutor(int corePoolSize, ThreadFactory factory) {
        super(corePoolSize, corePoolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                factory);
    }

    @Override
//    @SneakyThrows
    public void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
//        logger.error("IO Exception: " + Arrays.toString(t.getStackTrace()), t);

//        execute(r);
    }
}
