package com.example.isaiah.droidz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Isaiah on 4/18/15.
 */
public class Message {
    String message;
    boolean doShow;
    Paint paint;
    int showCounter;
    int x;
    int y;

    public Message(String message, boolean doShow, int x, int y){
        this.message = message;
        this.doShow =  doShow;
        this.showCounter = 0;
        this.x = x;
        this.y = y;

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
    }

    public void show(String message, int x, int y){
        this.doShow = true;
        this.message = message;
        this.showCounter = 0;
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas){
        if(showCounter>100){
            doShow = false;
            showCounter = 0;
        }

        if(doShow) {
            canvas.drawText(message, x, y, paint);
            showCounter++;
        }
    }
}
