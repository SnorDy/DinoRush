package com.example.dinorush;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class CloudSprite {
    private Bitmap bitmap;
    private int x,y,viewWidth;
    private boolean isActive;
    private  int frameWidth,frameHeight,Vx;

    private Paint paint =new Paint();

    public CloudSprite(Bitmap b, int x, int y, int Vx){
        this.bitmap=b;
        this.x = x;
        this.y = y;
        this.Vx = Vx;

    }
    public void update() {

        x -= Vx;
        if (x<-200){x= viewWidth+bitmap.getWidth();this.isActive=false;
            Log.d("UPDATE","Its update");}

    }
    public void setX(int x){this.x = x;}
    public void setY(int y){this.y = y;}
    public void setVx(int Vx){this.Vx=Vx;}
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, paint);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
