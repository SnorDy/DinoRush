package com.example.dinorush;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PterodactylSprite {
    private List<Rect> frames = new ArrayList<Rect>();
    private Bitmap bitmap;
    private int x,y,viewW;
    private boolean isActive;
    private  int frameWidth,frameHeight,Vx;
    private int currentFrame;
    private double timeForCurrentFrame;
    private double frameTime;
    private Paint paint = new Paint();
    public PterodactylSprite(Bitmap b, int x, int y,Rect initialFrame,int ViewW){
        this.bitmap =b;
        this.x=x;
        this.y=y;
        this.timeForCurrentFrame = 0.0;
        this.frameTime =60;
        this.currentFrame = 0;
        this.viewW= ViewW;
        this.frameWidth = initialFrame.width();
        this.frameHeight = initialFrame.height();

    }



    public boolean isActive() {
        return isActive;
    }
    public Rect getBoundingBoxRect(boolean jump){
        if (currentFrame%2==0)
        return new Rect(this.x,this.y+frameHeight/2,this.x+getFrameWidth(),this.y+frameHeight);
        return new Rect(this.x,this.y,this.x+getFrameWidth(),this.y+frameHeight/2);
    }
    public Rect getBoundingBoxRect(){
        if (currentFrame%2==0)
            return new Rect(this.x,this.y+frameHeight/2,this.x+getFrameWidth()-getFrameWidth()/4,this.y+frameHeight);
        return new Rect(this.x,this.y,this.x+getFrameWidth()-getFrameWidth()/4,this.y+frameHeight/2);
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    public void addFrame (Rect frame) {
        frames.add(frame);
    }
    public int getFrameWidth(){return frameWidth;}
    public void update(int ms){

        this.x -= Vx;
        timeForCurrentFrame += ms;
        if (timeForCurrentFrame >= frameTime&&isActive()) {
            currentFrame = (currentFrame + 1) % (frames.size());
            timeForCurrentFrame = timeForCurrentFrame - frameTime;
        }
        if (x<-getFrameWidth()){this.x= viewW+bitmap.getWidth();this.isActive=false;}}




    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public  int getX(){return this.x;}

    public void setVx(int vx) {
        if (vx!=0)
        Vx = vx+15;
        else Vx=0;
    }

    public int getVx() {
        return Vx;
    }
    public void draw(Canvas canvas) {
        Rect destination = new Rect(x, this.y, (x + getFrameWidth()), (this.y +frameHeight));
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination,  paint);
        Log.d("PTEROD1",getX()+" IN DRAW");

    }
}
