package com.example.myservicethread;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.os.Process;

// for each start request, we will use a worker thread to perform the job and
// processes only one request at a time.

public class MyService extends Service {

    /** interface for clients that bind */
    // IBinder mBinder;

    /** indicates whether onRebind should be used */
    boolean mAllowRebind;

    public MyService() {
        Log.i("myLog", "MyService()");
    }

    // Thread's Service Looper
    private Looper serviceLooper;

    // Handler that receives messages from the thread
    private ServiceHandler serviceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Log.i("myLog", "handleMessage() work started " + msg.arg1);
                Thread.sleep(5000);
                Log.i("myLog", "handleMessage() work accomplished " + msg.arg1);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    /** Called when the service is being created. */
    @Override
    public void onCreate() {
        Log.i("myLog", "onCreate()");

        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("myLog", "onStartCommand() " + startId);

        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        // indicates how to behave if the service is killed */
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        Log.i("myLog", "onDestroy()");
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("myLog", "onBind()");
        return null;
        // return mBinder;
    }

}
