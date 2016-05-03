package com.example.isaiah.droidz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Isaiah on 3/25/15.
 */
public class Obstacle {
    int x;
    int y;
    int vy;
    int height;
    boolean useful = true;
    Paint p = new Paint();
    boolean isUnk = false;
    int dim;

    public Obstacle(int x, int vy, int height, int dim, boolean reverse){
        this.x = x;
        this.vy = vy;
        this.height = height;
        this.dim = dim;

        if(reverse){
            this.y = height-20;
        }else{
            this.y = 40;
        }
    }

    public void move(int inc, boolean reverse){
        if(!reverse) {
            if (y < height - 20) {
                y = y + (vy + inc);
            } else {
                useful = false;
            }
        }else{
            if (y > 0) {
                y = y - (vy + inc);
            } else {
                useful = false;
            }
        }
    }

    public void draw(Canvas canvas){
        p.setColor(Color.GREEN);
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x+dim, (dim/2)+y, p);
    }

    public void recycle(int x, int vy, boolean reverse){
        this.x = x;
        this.vy = vy;

        if(reverse){
            this.y = height-20;
        }else{
            this.y = 40;
        }
    }

    public boolean amUnk(){
        return isUnk;
    }

    public boolean getAlive(){
        return useful;
    }

    public void setAlive(boolean use){
        this.useful = use;
    }

}
