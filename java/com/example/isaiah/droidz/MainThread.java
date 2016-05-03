package com.example.isaiah.droidz;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Date;

/**
 * Created by Isaiah on 3/25/15.
 */
public class MainThread extends Thread {
    Canvas canvas;
    private boolean running;
    private SurfaceHolder holder;
    private ViewerMain view;
    int tickCount = 0;
    private static final String TAG = MainThread.class.getSimpleName();
    int REAL_TICK_COUNT;
    long startTime;
    long endTime;


    public MainThread(SurfaceHolder holder, ViewerMain view){
        this.holder = holder;
        this.view = view;
    }

    public void setRunning(boolean running){
        this.running = running;
    }

    @Override
    public void run() {
        Log.d(TAG, "Starting game loop");
        startTime = (new Date()).getTime();
        while(running){
            canvas = null;
            try{
                canvas = this.holder.lockCanvas();
                synchronized (holder){
                    this.view.onDraw(canvas);
                }
            }finally{
                if(canvas != null){
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            //update
            //render
            tickCount++;
            REAL_TICK_COUNT++;
        }



        endTime = (new Date()).getTime();

        Log.d(TAG, "Game loop executed " + REAL_TICK_COUNT + " times.");

        Log.d(TAG, "FPS: "+FPS());

        interrupt();
    }

    private double FPS(){
        long elapsed = (new Date()).getTime() - startTime;
        return (REAL_TICK_COUNT/(elapsed/1000.0));
    }

    public void jumpCount() {
        tickCount+=50;
    }

    public void dockCount(){
        tickCount-=150;
    }

    public void jumpCountBy(int jump){
        tickCount+=jump;
    }

    public int getREAL_TICK_COUNT(){ return REAL_TICK_COUNT; };
}
