package com.example.dinorush;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class CoinsSprite {
    private Bitmap bitmap;
    private int x,y,Vx,viewWidth;

    private boolean isActive=false;
    public boolean isActive() {
        return isActive;
    }
    Paint paint = new Paint();
    public  CoinsSprite(Bitmap bitmap,int x,int y,int Vx,int viewW){
        this.bitmap= bitmap;
        this.x= x;
        this.y= y;
        this.Vx = Vx;
        this.viewWidth = viewW;

    }
    public void update() {

        x -= Vx;
        if (x<-200){x= viewWidth+bitmap.getWidth();this.isActive=false;
            Log.d("UPDATE","Its update");}

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

    public void setVx(int Vx){this.Vx=Vx;}
    public int getWidth(){return bitmap.getWidth();}

    public int getX() {
        return x;
    }
    public int getVx() {
        return Vx;
    }
    public void setX(int x){this.x = x;}
    public void setActive(boolean f){this.isActive=f;}
    public void setY(int y){this.y = y;}

}
