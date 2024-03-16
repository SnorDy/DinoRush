package com.example.dinorush;

import android.content.Context;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.graphics.Paint;

import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Random;

public class DrawThread extends Thread  {

    public double speed =40;
    public int start_speed = 40;
    public int restart_width,restart_height,restart_x,restart_y,btn_home_x;
    public DinoSprite Dino;
    public PterodactylSprite Pterod;
    public double points;
    private int coins;
    private boolean pterod_is_ready = false;
    private int bestScore;
    private  final CoinsSprite Coin;
    private final DataHelper DBconnector;
    private Bitmap tree_bitmap;
    private Bitmap[] cactuses;
    private ArrayList<Bitmap> tree_bitmap_arr = new ArrayList<>();
    private CloudSprite[] clouds_arr=new CloudSprite[4];
    private String mode = "cactus";
    private Bitmap background_bitmap, dino_bitmap,bent_dino_bitmap,restart_bitmap,
            coin_bitmap,menu_bitmap,pterod_bitmap,cloud_bitmap;
    private int background_x = 0;
    private int  jump_length=1200;//длина прыжка для контроля спавна елок
    private final int timerInterval = 10;
    private Sprite[] trees = new Sprite[2];
    private SurfaceHolder surfaceHolder;
    private  int viewWidth,viewHeight;
    private Paint paint= new Paint();
    private long prevTime;
    private int background_color;
    private volatile boolean running = true; //флаг для остановки потока

    public DrawThread( Context context,SurfaceHolder surfaceHolder,int w,int h) {

        this.surfaceHolder = surfaceHolder;
        DBconnector = new DataHelper(context);
        bestScore = DBconnector.select();
        coins = DBconnector.selectCoins();
        Typeface tf = ResourcesCompat.getFont(context, R.font.pixel_font);
        paint.setTextSize(120);
        paint.setTypeface(tf);

        prevTime = System.currentTimeMillis();//Время на начало потока
        viewHeight=h;
        viewWidth=w;
        //инициализация изображений спрайтов
        Bitmap b = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.background2);
        background_bitmap = Bitmap.createScaledBitmap(b, b.getWidth(), viewHeight - 2*viewHeight / 3, false);
        tree_bitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.tree1), viewWidth / 14, viewHeight / 4, false);
        pterod_bitmap =Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.pterodactyl), viewWidth / 4, viewHeight / 3, false);
        dino_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.green_dino), viewWidth / 2 + viewWidth / 20, viewHeight / 4, false);
        bent_dino_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.green_dino_down), viewWidth / 4, viewHeight / 6, false);
        coin_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.coin), viewWidth / 30, viewWidth / 30, false);
        cloud_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.cloud), viewWidth / 7, viewWidth / 13, false);

        if (this.mode == "cactus") {
        cactuses = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(),
                R.drawable.cactus_1),BitmapFactory.decodeResource(context.getResources(),
                R.drawable.cactus_2), BitmapFactory.decodeResource(context.getResources(),
                R.drawable.cactus_4),BitmapFactory.decodeResource(context.getResources(),
                R.drawable.cactus_5),BitmapFactory.decodeResource(context.getResources(),
                R.drawable.cactus_6),BitmapFactory.decodeResource(context.getResources(),
                R.drawable.cactus_7)};

        for (int i=0;i< cactuses.length;i++){this.tree_bitmap_arr.add(Bitmap.createScaledBitmap(cactuses[i],
                viewWidth / 14*cactuses[i].getWidth()/100,
                viewHeight / 7*cactuses[i].getHeight()/100,false));}}
        else{tree_bitmap_arr.add( Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.tree1), viewWidth / 14, viewHeight / 4, false));
        }

        int dino_w = dino_bitmap.getWidth() / 6;//деление одной картнки на кадры
        int bent_dino_w = bent_dino_bitmap.getWidth()/2;
        int pterod_w= pterod_bitmap.getWidth()/2;

        Rect firstFrame = new Rect(0, 0, dino_w, dino_bitmap.getHeight());//первый кадр с началом в 0 0
        Rect firsPterodFrame = new Rect(0,0,pterod_w,pterod_bitmap.getHeight());

        Pterod = new PterodactylSprite(pterod_bitmap,pterod_w+viewWidth,viewHeight/7,firsPterodFrame,viewWidth);
        Dino = new DinoSprite(dino_bitmap, bent_dino_bitmap,40, 4*viewHeight/7, firstFrame);
        Coin = new CoinsSprite(coin_bitmap,0,viewHeight - viewHeight / 4-viewHeight / 98-50,(int) this.speed,viewWidth);
        //добавление прямоугольников в массив
        for (int i = 0; i < 6; i++) {
            Dino.addFrame(new Rect(i * dino_w, 0, i * dino_w + dino_w, dino_bitmap.getHeight()));
        }
        for (int i = 0;i<2;i++){Dino.addBentFrame(new Rect(i * bent_dino_w, 0,
                i * bent_dino_w + bent_dino_w, bent_dino_bitmap.getHeight()));}

        for (int i = 0;i<2;i++){Pterod.addFrame(new Rect(i * pterod_w, 0,
                i * pterod_w + pterod_w, pterod_bitmap.getHeight()));}
        Random random = new Random();
        for (int i=0;i<4;i++){clouds_arr[i]=new CloudSprite(cloud_bitmap,viewWidth+10,
                random.nextInt(3*viewHeight/7)+cloud_bitmap.getHeight(),(int) this.speed);}

        //вычисление размеров и положения кнопки рестарт
        restart_width=viewWidth/11;
        restart_height=viewHeight/5;
        restart_x=viewWidth/2-restart_width-20;
        btn_home_x=restart_x+20+restart_width;
        restart_y=viewHeight/2-restart_height/4;
        //проверка на ночной режим и изменение цвета текста и изображенмия кнопки рестарт в соотвветствии с темой
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                background_color = context.getResources().getColor(R.color.white);
                paint.setColor(context.getResources().getColor(R.color.light_theme_color));
                restart_bitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.restart_light),restart_width,restart_height,false);
                menu_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.menu_light),restart_width,restart_height,false);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                background_color = context.getResources().getColor(R.color.black);
                paint.setColor(context.getResources().getColor(R.color.dark_theme_color));
                restart_bitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.restart_dark),restart_width,restart_height,false);
                menu_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.menu_dark),restart_width,restart_height,false);break;

        }
    }

    public int getBtnX(){return restart_x;}
    public int getBtnHomeX(){return btn_home_x;}
    public int getBtnY(){return restart_y;}
    public int getBtnWidth(){return restart_width;}
    public int getBtnHeight(){return restart_height;}

    public void requestStop() {
        running = false;
    }

    protected void update() {
        this.speed+=0.005;
        if ((points%50==0) && points!=0) {
            this.speed += 2;
            Dino.updateFrameTime();
        }
        //проверка столкновений с елками
        for (Sprite tree : trees) {
            tree.Vx=(int)speed;
            Dino.intersect(tree);
        }
        //если врезался, обнуляем скорость движения
        if (!Dino.isAlive()) {
            Coin.setActive(false);
            Pterod.setActive(false);
            pterod_is_ready=false;
            DBconnector.updateCoins(coins);
            this.speed = 0;
            if (bestScore<(int)points){
                //сохранение нового рекорда
                DBconnector.updateRecord((int) points);
                bestScore=(int) points;
            }
        }

        if (this.speed==0){Dino.setAlive(false);}
        //проверка на то, что дино живой
        if (Dino.isAlive()) {
            if (Coin.isActive())Coin.update();
            if (Pterod.isActive()) {
                Pterod.update(timerInterval);
                if (!Pterod.isActive())
                    pterod_is_ready=false;
                    GenerateTrees();
            }

            if (Dino.intersect(Coin)&&Coin.isActive()){Coin.setActive(false);coins+=1;}
            if (Dino.intersect(Pterod)&&Pterod.isActive()){Dino.setAlive(false);Pterod.setActive(false);}

            background_x = (background_x + (int) this.speed) % (background_bitmap.getWidth());

            for (Sprite tree : trees) {
                tree.update();
            }

            points+=0.05;
            Dino.update(timerInterval);
        }

    }
    public void setPterod_is_ready(boolean f){pterod_is_ready=f;}
    public void GenerateTrees(){

        int last_coords = 0;
        Random random=new Random();

        for (int i = 0; i < 2; i++) {
            int coords = random.nextInt(viewWidth/2) + last_coords+jump_length+viewWidth;
            int ind = random.nextInt(tree_bitmap_arr.size());
            if (this.mode == "tree"){
                trees[i] = new ChristmasTreeSprite(tree_bitmap, coords, viewHeight - viewHeight / 2 + viewHeight / 17, (int) this.speed, viewWidth);}
            else{
                trees[i] = new CactusSprite(tree_bitmap_arr.get(ind),coords , 3*viewHeight/4+viewHeight/14, (int) this.speed, viewWidth);}
            last_coords=coords;

        }

    }

    @Override
    public void run() {
        paint.setTextSize(80);
        //добавление елок в массив
        GenerateTrees();

        Paint paint2= new Paint();
        paint2.setColor(background_color);
        while (running) {

            Canvas canvas = surfaceHolder.lockCanvas(null);


            if (canvas != null) {
                try {
                    canvas.drawRect(new Rect(0,0,viewWidth,viewHeight),paint2);

                    long now = System.currentTimeMillis();
                    long elapsedTime = now - prevTime;

                    //отрисовка зднего фона (двумя частями, так как он двигается) чтобы не оставалось пустого пространства
                    canvas.drawBitmap(background_bitmap, -background_x, viewHeight - viewHeight / 4-viewHeight / 98, paint);
                    canvas.drawBitmap(background_bitmap, -background_x+background_bitmap.getWidth(), viewHeight - viewHeight / 4-viewHeight / 98, paint);

                    canvas.drawBitmap(coin_bitmap, 0,60, paint);
                    canvas.drawText(""+coins,viewWidth/30+10,110,paint);
                    canvas.drawText("max: "+bestScore,viewWidth-260,80,paint);
                    canvas.drawText((int) points + "", viewWidth - 260, 40, paint);
                    Random random = new Random();
                    //если елка за пределами экрана,то генерируем новую
                    int last_coords=0;
                    if (!pterod_is_ready&&!Pterod.isActive()){//если птеро не собирается вылетать и не летит уже, то обновляем положение елок или кактусов
                    for (int i = 0; i < 2; i++) {
                        if (!(trees[i].isActive())) {//создаем новую елку если она за пределами экрана, т е не активна
                            if (i == 0) last_coords = trees[1].getX();
                            else last_coords = trees[0].getX();
                            int coords = random.nextInt(viewWidth / 2) + last_coords + jump_length;
                            int ind = random.nextInt(tree_bitmap_arr.size());
                            if (mode == "tree") {
                                trees[i] = new ChristmasTreeSprite(tree_bitmap, coords, viewHeight - viewHeight / 2 + viewHeight / 17, (int) this.speed, viewWidth);
                            } else {
                                trees[i] = new CactusSprite(tree_bitmap_arr.get(ind), coords, 3*viewHeight/4+viewHeight/14, (int) this.speed, viewWidth);
                            }
                        } else {
                            trees[i].draw(canvas);
                        }
                    }}
                    else{for (int i = 0; i < 2; i++)if (Dino.isAlive())trees[i].draw(canvas);//если птеро готов к вылету, ждем пока последняя видимая елка скроется с экрана
                        if (Math.max(trees[0].getX()+trees[0].getFrameWidth(),trees[1].getX()+trees[1].getFrameWidth())<0){Pterod.setActive(true);
                            Pterod.setVx((int) this.speed);}}

                        if (now % 100 == 0 && Dino.isAlive() && !(Coin.isActive())&&Math.max(trees[0].getX(), trees[1].getX())>viewWidth) {//спавн монетки каждые 100 мс
                            last_coords = random.nextInt((Math.max(trees[0].getX(), trees[1].getX())- Math.min(trees[0].getX(), trees[1].getX()))/2)+ Math.min(trees[0].getX(), trees[1].getX()) ;
                            if (last_coords>viewWidth){
                            Coin.setX(last_coords);
                            Coin.setActive(true);
                            Coin.setVx((int) this.speed);}
                        }

                        if (Coin.isActive()) Coin.draw(canvas);

                        if (now%257==0 &&Dino.isAlive()&&!(Pterod.isActive())){
                            pterod_is_ready=true;
                            Pterod.setX(viewWidth+Pterod.getFrameWidth());//спавн птеродактеля

                        Pterod.setY(random.nextInt(3*viewHeight/7-(int)( bent_dino_bitmap.getHeight()))+bent_dino_bitmap.getHeight()/2);
                        }
                        if (Pterod.isActive())Pterod.draw(canvas);
                        Dino.draw(canvas);


                        //проверка на конец игры
                        if (!Dino.isAlive()) {
                            paint.setTextSize(120);
                            canvas.drawText("GAME OVER", viewWidth / 2 - 375, viewHeight / 3, paint);
                            canvas.drawBitmap(restart_bitmap, restart_x, restart_y, paint);
                            canvas.drawBitmap(menu_bitmap, btn_home_x, restart_y, paint);
                            paint.setTextSize(80);

                            Pterod.draw(canvas);




                        }
                        if (elapsedTime > 20) {

                            update();
                            prevTime = now;
                        }


                    } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }

        }
    }}


}
