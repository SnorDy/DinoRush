package com.example.dinorush;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class CactusSprite extends Sprite {
    private Bitmap bitmap;
    Paint paint = new Paint();

    public CactusSprite(Bitmap b, int x, int y, int Vx,int viewW) {
        super(b,x,y,Vx,viewW);
        this.bitmap = b;

    }

    public Rect getBoundingBoxRect(){
        return new Rect(this.x,this.y-bitmap.getHeight(),this.x+bitmap.getWidth(),this.y);
    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y-bitmap.getHeight(), paint);
    }

}