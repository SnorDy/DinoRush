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
    public Sprite(Bitmap b){this.bitmap=b;}
    public boolean isActive() {
        return isActive;
    }
    public int getFrameWidth(){return bitmap.getWidth();}
    public void update() {

        x -= Vx;
        if (x< -200){this.isActive=false;
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



    public int getX() {
        return x;
    }
}

