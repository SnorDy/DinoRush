package com.example.dinorush;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MotionEventCompat;

import java.util.Random;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    private static int viewWidth, viewHeight;

    private MediaPlayer jumpPlayer;
    DrawThread drawThread;
    private int speed = 70,restart_width,restart_height,restart_x,restart_y,btn_home_x;


    final private String best_points="0";
    private Bitmap background_bitmap, tree_bitmap, dino_bitmap,restart_bitmap;
    private int background_x = 0;
    private static int  jump_length=1200;//длина прыжка для контроля спавна елок
    private final int timerInterval = 30;
    private static ChristmasTreeSprite[] trees = new ChristmasTreeSprite[2];
    Paint paint = new Paint();

    public DrawView(Context context) {
        super(context);
        jumpPlayer= MediaPlayer.create(context, R.raw.jump);
        getHolder().addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {


        // создание SurfaceView





    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("WIDTH",height+ " "+ width);
        viewHeight=height;
        viewWidth=width;

        drawThread = new DrawThread(getContext(),getHolder(),viewWidth,viewHeight);
        drawThread.start();
        restart_width=drawThread.getBtnWidth();
        restart_height=drawThread.getBtnHeight();
        restart_x=drawThread.getBtnX();
        restart_y=drawThread.getBtnY();
        btn_home_x = drawThread.getBtnHomeX();



        // изменение размеров SurfaceView

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.requestStop();
        boolean retry = true;
        while (retry) {

            try {
                drawThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;

        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int eventAction = MotionEventCompat.getActionMasked(event);
        Log.d("ACTION",eventAction+"");
        if (!drawThread.Dino.isAlive()){
        //проверка на нажатие кнопки рестарт

            if ((restart_x<=event.getX()&&event.getX()<=restart_x+restart_width&&restart_y<=event.getY()&&event.getY()<=restart_y+restart_height)){
                drawThread.Dino.setAlive(true);
                drawThread.Pterod.setActive(false);
                drawThread.Pterod.setX(viewWidth+drawThread.Pterod.getFrameWidth());
                drawThread.speed=drawThread.start_speed;
                drawThread.points=0;
                drawThread.setPterod_is_ready(false);
                drawThread.GenerateTrees();}
            else if (btn_home_x<=event.getX()&&event.getX()<=btn_home_x+restart_width&&restart_y<=event.getY()&&event.getY()<=restart_y+restart_height){
                Intent intent = new Intent(getContext(),StartActivity.class);
                getContext().startActivity(intent);

                }}

            else {
            if (eventAction == MotionEvent.ACTION_DOWN && !drawThread.Dino.IsDown() && !drawThread.Dino.IsUp() && event.getX() >= viewWidth / 2) {
                //прыжок
                drawThread.Dino.SetUp(true);
                jumpPlayer.start();
            }if (eventAction == MotionEvent.ACTION_DOWN && !drawThread.Dino.IsDown() && !drawThread.Dino.IsUp() && event.getX() < viewWidth / 2) {
                //пригиб
                drawThread.Dino.setBent(true);
            }
            if ((eventAction == MotionEvent.ACTION_UP|eventAction==MotionEvent.ACTION_CANCEL)&&drawThread.Dino.isBent()) {
                drawThread.Dino.setBent(false);

            }
        }


        return true;
    }




    // уничтожение SurfaceView

    }
