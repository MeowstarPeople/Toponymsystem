package toponymsystem.island.com.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {

    private ExecutorService mExecutorService;

    private ThreadPoolManager() {
        mExecutorService = Executors.newCachedThreadPool();
    }

    private static final ThreadPoolManager mThreadPoolManager = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return mThreadPoolManager;
    }

    public void executeTask(Runnable runnable) {
        mExecutorService.execute(runnable);
    }


}
