package com.example.isaiah.droidz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Isaiah on 3/26/15.
 */
public class PotentialLife {
    int x;
    int y = 40;
    boolean exists = false;
    int height;
    Paint paint = new Paint();

    public PotentialLife(int x, int height){
        this.x = x;
        this.height = height;

    }

    public void move(int inc){
        y+=(6+inc);

        if(y>height){
            setExists(false);
        }
    }

    public void draw(Canvas canvas){

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(x, y, x+20, y+10, paint);
    }

    public boolean isReal(){
        return exists;
    }

    public void setExists(boolean bool){
        exists = bool;
    }
}
