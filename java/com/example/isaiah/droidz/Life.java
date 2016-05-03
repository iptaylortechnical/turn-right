package com.example.isaiah.droidz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Isaiah on 3/26/15.
 */
public class Life {
    int x;
    int y = 10;
    boolean exists = true;


    public Life(int x){
        this.x = x;
    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x+30, y+30, paint);
    }

    public boolean getExists(){
        return exists;
    }

    public void setExists(boolean ex){
        this.exists = ex;
    }
}
