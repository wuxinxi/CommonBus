package szxb.com.commonbus.util.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 作者: Tangren on 2017/8/15
 * 包名：com.szxb.buspay.task
 * 邮箱：996489865@qq.com
 * TODO:单利任务调度线程池
 */

public class ThreadScheduledExecutorUtil {

    private static volatile ThreadScheduledExecutorUtil mThreadScheduledExecutorUtil = null;

    private static ScheduledExecutorService service;

    private ThreadScheduledExecutorUtil() {
        service = Executors.newScheduledThreadPool(5);
    }

    public static ThreadScheduledExecutorUtil getInstance() {
        synchronized (ThreadScheduledExecutorUtil.class) {
            if (mThreadScheduledExecutorUtil == null) {
                mThreadScheduledExecutorUtil = new ThreadScheduledExecutorUtil();
            }
        }
        return mThreadScheduledExecutorUtil;
    }

    public ScheduledExecutorService getService() {
        return service;
    }

    public void setService(ScheduledExecutorService service) {
        this.service = service;
    }

    public static void shutdown() {
        if (service != null && !service.isShutdown()) {
            service.shutdown();
        }
    }
}
