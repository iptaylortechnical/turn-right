package com.example.isaiah.droidz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Isaiah on 3/28/15.
 */
public class Unknown extends Obstacle {
    int col;
    int lastY;
    int[] colB = {Color.BLUE, Color.WHITE, Color.YELLOW, Color.RED, Color.LTGRAY};
    int[] colIt = {1, 2, 3, 4, 0};
    int colCounter = 1;
    boolean isUnk = true;


    public Unknown(int x, int vy, int height, int dim, boolean reverse){
        super(x, vy, height, dim, reverse);
        col = Color.RED;
        lastY = 0;
        this.dim = dim;
    }

    @Override
    public void draw(Canvas canvas) {
        color();
        p.setColor(col);
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x+dim, y+(dim/2), p);
    }

    public void color(){
        if(y-lastY > 125) {
            lastY = y;
            col = colB[colCounter];
            colCounter = colIt[colCounter];
        }
    }

    @Override
    public boolean amUnk() {
        return isUnk;
    }
}
