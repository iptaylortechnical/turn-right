package com.example.isaiah.droidz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Isaiah on 3/25/15.
 */
public class Pawn {
    private int x = 200;
    private int y = 700;
    int vx = 5;
    int vy = 0;
    int dir = 3;
    int[] dirB = {1,2,3,0};
    int[] dirB_reverse_y = {2,0,0};
    int[] dirB_reverse_x = {0,3,0,1};
    int scrnheight;
    int scrnwidth;
    boolean alive = true;
    int dim;
    private boolean isReverseMode = false;


    public boolean getAlive(){
        return alive;
    }

    public void setAlive(boolean al){
        this.alive = al;
    }

    public boolean getIsReverseMode(){
        return isReverseMode;
    }

    public void setIsReverseMode(boolean bool){
        isReverseMode = bool;
    }

    public int getX(){return x;}

    public void setX(int x){
        this.x = x;
    }

    public int getY(){return y;}

    public void setY(int y){this.y = y;}


    public Pawn(int height, int width, int dim){
        this.scrnheight = height;
        this.scrnwidth = width;
        this.dim = dim;
    }

    public void move(){
        if(x>0 && y>0 && x<scrnwidth-dim && y<scrnheight-dim) {
            x += vx;
            y += vy;
        }else{
            alive = false;
        }
    }

    public void setDir(int dir, int inc) {

        switch(dir){
            case 1:
                //left
                vx = -5 - inc;
                vy = 0;
                break;
            case 2:
                //up
                vy = -5 - inc;
                vx = 0;
                break;
            case 3:
                //right
                vx = 5 + inc;
                vy = 0;
                break;
            case 0:
                //down
                vy = 5 + inc;
                vx = 0;
                break;
            default:
                break;
        }

    }

    public void handleTap(int inc){
        dir = dirB[dir];
        setDir(dir, inc);

        if(isReverseMode){
            if(dir == 0 || dir == 2){
                dir = dirB_reverse_y[dir];
            }else{
                dir = dirB_reverse_x[dir];
            }
        }
    }

    public void draw(Canvas canvas, int col){
        Paint p = new Paint();
        p.setColor(col);
        p.setStyle(Paint.Style.FILL);

        canvas.drawRect(x, y, x+dim, y+dim, p);
    }

}