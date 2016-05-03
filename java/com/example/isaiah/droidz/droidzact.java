package com.example.isaiah.droidz;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.isaiah.droidz.R;

public class droidzact extends Activity /*implements dieListener*/{

    private static final String TAG = droidzact.class.getSimpleName();
    private Button button;
    Context con;
    Activity ac = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);




        setContentView(R.layout.activity_droidzact);
        button = (Button) findViewById(R.id.goB);
        con = this;



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                ViewerMain theview = new ViewerMain(con, ac);
               /* dieListener lis = new dieListener() {
                    @Override
                    public void surfaceFinished() {
                        setContentView(R.layout.activity_droidzact);
                    }
                };
                theview.addListener(lis);*/
                setContentView(theview);
                Log.d(TAG, "View added");
                theview.getHolder().addCallback(new SurfaceHolder.Callback() {


                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {

                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {
                        setContentView(R.layout.activity_droidzact);
                    }


                });
//                try {
//                    theview.thread.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                Log.d(TAG, "second falsified");
            }
        });

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopping...");
        super.onStop();
    }



    public void reset(){
        //setContentView(R.layout.activity_droidzact);
    }

   /* @Override
    public void surfaceFinished() {
        setContentView(R.layout.activity_droidzact);
    }*/
}