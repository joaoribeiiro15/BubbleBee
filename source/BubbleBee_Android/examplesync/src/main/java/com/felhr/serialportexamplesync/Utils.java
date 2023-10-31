package com.felhr.serialportexamplesync;
import android.os.Handler;

public class Utils {


    public interface DelayCallback{
        void afterDelay();
    }

    public static void delay(int tempo, final DelayCallback delayCallback){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, tempo); // afterDelay will be executed after (secs*1000) milliseconds.
    }
}
