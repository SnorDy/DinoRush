package com.example.dinorush;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DinoSprite {
    private Bitmap bitmap,bent_bitmap;
    private int x,y;
    private boolean isAlive=true,isBent = false;
    private boolean up = false,down=false;


    private List<Rect> frames;
    private List<Rect> bent_frames = new ArrayList<Rect>();
    private int frameWidth;
    private int jump_y,start_y;
    private int frameHeight;
    private int currentFrame;
    private double timeForCurrentFrame;
    private double frameTime;
    public int jump_speed;
    private Paint p = new Paint();
    public void setBent(boolean f){this.isBent= f;}
    public boolean isBent(){return this.isBent;}
    public DinoSprite(Bitmap b,Bitmap b1, int x, int y, Rect initialFrame){

        this.bitmap=b;
        this.bent_bitmap = b1;
        this.x=x;
        this.jump_speed=45;

        this.y= y;
        this.start_y=this.y;
        this.jump_y=this.y%jump_speed+frameHeight-(frameHeight%jump_speed);
        this.frames = new ArrayList<Rect>();
        this.frames.add(initialFrame);

        this.timeForCurrentFrame = 0.0;
        this.frameTime =30;
        this.currentFrame = 0;
        this.frameWidth = initialFrame.width();
        this.frameHeight = initialFrame.height();


    }
    public void updateFrameTime(){this.frameTime-=2; if (this.frameTime<20)this.frameTime=20;}//метод для ускорения анимации в соответствии со скоростью
    public boolean IsDown() {
        return down;
    }

    public void SetDown(boolean down) {
        this.down = down;
    }

    public boolean IsUp() {
        return up;
    }

    public void SetUp(boolean up) {
        this.up = up;
    }


    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }


    public void addFrame (Rect frame) {
        frames.add(frame);
    }
    public void addBentFrame(Rect frame){bent_frames.add(frame);}
    public int getY(){return y;}

    public void update(int ms){
        timeForCurrentFrame += ms;

        //реализация прыжка
        if ((IsUp())&&(this.y!=jump_y))this.y-=jump_speed;
        else {SetDown(true);SetUp(false);}

        if ((this.y!=this.start_y)&&(IsDown())){this.y+=jump_speed;}
        else {SetDown(false);}


        if (timeForCurrentFrame >= frameTime&&isAlive()&&!(isBent())) {
            currentFrame = (currentFrame + 1) % (frames.size()-2);
            timeForCurrentFrame = timeForCurrentFrame - frameTime;
        }
        else if (timeForCurrentFrame >= frameTime&&isAlive()&&isBent()) {
            currentFrame = (currentFrame + 1) % (bent_frames.size());
            timeForCurrentFrame = timeForCurrentFrame - frameTime-10;
        }


    }
    public boolean intersect(Sprite s){//проврека на столкновения с елкой

        if (getBoundingBoxRect().intersect(s.getBoundingBoxRect())) {setAlive(false);
            currentFrame=frames.size()-1;
            setBent(false);

            return true;}

        return false;
    }

    public boolean intersect(CoinsSprite s){//проврека на столкновения с елкой

        if (getBoundingBoxRect().intersect(s.getBoundingBoxRect())) return true;

        return false;
    }
    public boolean intersect(PterodactylSprite s){//проврека на столкновения с птеродактелем
        if (y!=start_y) {
            if (getBoundingBoxRect(1).intersect(s.getBoundingBoxRect(true))) {
                currentFrame = frames.size() - 1;
                setBent(false);
                return true;
            }} else {
                if (getBoundingBoxRect(1).intersect(s.getBoundingBoxRect())) {
                    currentFrame = frames.size() - 1;
                    setBent(false);
                    return true;
                }
            }

            return false;
        }
    public Rect getBoundingBoxRect(int x){
        if (!(isBent())){
        return new Rect(this.x,this.y,this.x+frameWidth-4,this.y+frameHeight);}
        else{ int y1= this.y+(bent_frames.get(0).height()/2);
            return  new Rect((int)x, (int) y1, (int)(x + bent_frames.get(0).width()), (int)y1+bent_frames.get(0).height());}
    }

    public Rect getBoundingBoxRect(){
        if (this.y!=this.start_y&&this.start_y>=this.y+10)return new Rect(this.x+75,this.y,this.x+frameWidth-90,this.y+frameHeight);
        return new Rect(this.x,this.y,this.x+frameWidth-15,this.y+frameHeight);
    }
    public void draw (Canvas canvas) {
        if (isBent()){
            int y1= this.y+(bent_frames.get(0).height()/2);
            Rect destination = new Rect((int)x, (int) y1, (int)(x + bent_frames.get(0).width()), (int)y1+bent_frames.get(0).height());
            canvas.drawBitmap(bent_bitmap, bent_frames.get(currentFrame%2), destination,  p);

        }
        else{

        Rect destination = new Rect((int)x, (int)this.y, (int)(x + frameWidth), (this.y +frameHeight));
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination,  p);
       }
    }
}
