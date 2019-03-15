package com.dreampany.frame.misc;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;

import com.dreampany.frame.util.AndroidUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 26/4/18.
 * Dreampany
 * dreampanymail@gmail.com
 */

@Singleton
public class AppExecutors {

    private static final int THREAD_COUNT = 3;

    private final UiThreadExecutor uiExecutor;

    private final Executor diskIO;

    private final Executor networkIO;

    @Inject
    public AppExecutors() {
        this(
                new UiThreadExecutor(),
                new DiskIOThreadExecutor(),
                new NetworkIOThreadExecutor()
        );
    }

    public AppExecutors(UiThreadExecutor uiThread, Executor diskIO, Executor networkIO) {
        this.uiExecutor = uiThread;
        this.diskIO = diskIO;
        this.networkIO = networkIO;
    }

    public Executor getUiExecutor() {
        return uiExecutor;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public void postToUi(Runnable run) {
        uiExecutor.execute(run);
    }

    public void postToUi(Runnable run, long delay) {
        uiExecutor.executeUniquely(run, delay);
    }

    public void postToUiSmartly(Runnable runnable) {
        if (AndroidUtil.isOnUiThread()) {
            runnable.run();
        } else {
            uiExecutor.execute(runnable);
        }
    }

    public boolean postToNetwork(Runnable run) {
        networkIO.execute(run);
        return true;
    }

    private static class UiThreadExecutor implements Executor {
        private Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
/*            if (AndroidUtil.isOnUiThread()) {
                command.run();
            } else {
                handler.post(command);
            }*/
            handler.post(command);
        }

        public void executeUniquely(@NonNull Runnable command) {
/*            if (AndroidUtil.isOnUiThread()) {
                command.run();
            } else {
                handler.post(command);
            }*/
            handler.removeCallbacks(command);
            handler.post(command);
        }

        public void execute(@NonNull Runnable command, long delay) {
            handler.postDelayed(command, delay);
        }

        public void executeUniquely(@NonNull Runnable command, long delay) {
            handler.removeCallbacks(command);
            handler.postDelayed(command, delay);
        }
    }

    private static class DiskIOThreadExecutor implements Executor {
        private final Executor diskIO;

        public DiskIOThreadExecutor() {
            diskIO = Executors.newSingleThreadExecutor();
        }

        @Override
        public void execute(@NonNull Runnable command) {
            diskIO.execute(command);
        }
    }

    private static class NetworkIOThreadExecutor implements Executor {
        private final Executor networkIO;

        public NetworkIOThreadExecutor() {
            networkIO = Executors.newFixedThreadPool(THREAD_COUNT);
        }

        @Override
        public void execute(@NonNull Runnable command) {
            networkIO.execute(command);
        }
    }
}
