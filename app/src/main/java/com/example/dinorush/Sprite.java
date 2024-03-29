package com.example.dinorush;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Sprite {
    private Bitmap bitmap;
    private boolean isActive=true;
    public int x, y, Vx,viewWidth;
    private int frameWidth;
    private Paint paint = new Paint();
    public Sprite(Bitmap b,int x, int y, int Vx,int viewW){this.bitmap=b;
        frameWidth = b.getWidth();
        this.x=x;
        this.y=y;
        this.Vx=Vx;
        this.viewWidth= viewW;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean f){this.isActive=f;}
    public int getFrameWidth(){return frameWidth;}

    public void update() {
        x -= Vx;
        if (x< -getFrameWidth())this.isActive=false;
    }
    public int getVx() {
        return Vx;
    }

    public Rect getBoundingBoxRect(){
        return new Rect(this.x,this.y,this.x+bitmap.getWidth(),this.y+bitmap.getHeight());
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, paint);
    }

    public int getY() {
        return y;
    }



    public int getX() {
        return x;
    }
}