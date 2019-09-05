package com.dreampany.framework.misc;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hawladar Roman on 6/6/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public abstract class Runner implements Runnable {

    protected final long delayS = TimeUnit.SECONDS.toMillis(1);
    protected long wait = delayS;
    protected Map<String, Long> times;
    protected Map<String, Long> delays;


    private volatile Thread thread;
    private volatile boolean running;
    private final Object guard = new Object();
    private volatile boolean guarded;

    protected Runner() {
        times = Maps.newHashMap();
        delays = Maps.newHashMap();
    }

    public void start() {
        startThread();
    }

    public void stop() {
        stopThread();
    }

    protected void startThread() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    protected void stopThread() {
        if (!running) {
            return;
        }
        running = false;
        thread.interrupt();
        notifyRunner();
    }

    public void interrupt() {
        if (thread == null || thread.isInterrupted() || (!thread.isAlive() && !thread.isDaemon())) {
            return;
        }
        thread.interrupt();
    }

    public void waitRunner(long timeoutMs) {
        if (guarded) {
            //return;
        }
        guarded = true;
        synchronized (guard) {
            try {
                if (timeoutMs > 0L) {
                    guard.wait(timeoutMs);
                } else {
                    guard.wait();
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public void notifyRunner() {
        if (!guarded) {
            //return;
        }
        guarded = false;
        synchronized (guard) {
            guard.notify();
        }
    }

    protected void silentStop() {
        if (running) {
            running = false;
        }
    }

    public boolean isExpired(long time, long delay) {
        return time + delay < System.currentTimeMillis();
    }

    public boolean isRunning() {
        return running;
    }

    private void waitFor(long timeout) throws InterruptedException {
        Thread.sleep(timeout);
    }

    protected boolean looping() throws InterruptedException {
        return false;
    }

    @Override
    public void run() {
        try {
            while (running) {
                boolean looped = looping();
                if (!looped) {
                    break;
                }
            }
        } catch (InterruptedException ignored) {
        }
        running = false;
    }
}
