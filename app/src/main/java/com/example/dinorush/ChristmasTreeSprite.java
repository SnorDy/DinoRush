package com.example.dinorush;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class ChristmasTreeSprite extends Sprite {
    private Bitmap bitmap;
    public int x, y, Vx,viewWidth;

    public boolean isActive() {
        return isActive;
    }


    private boolean isActive=true;
    Paint paint = new Paint();

    public ChristmasTreeSprite(Bitmap b, int x, int y, int Vx,int viewW) {
        super(b);
        this.bitmap=b;
        this.x = x;
        this.y = y;
        this.Vx = Vx;
        this.viewWidth=viewW;
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
