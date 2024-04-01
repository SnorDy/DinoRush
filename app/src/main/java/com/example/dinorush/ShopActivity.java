package com.example.dinorush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShopActivity extends AppCompatActivity {
    private ArrayList<Button> btn_arr = new ArrayList<Button>();
    private Map<String, Bitmap> bitmap_sl = new HashMap<String, Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_shop);
        String[] bitmap_keys = {"green","blue","purple"};
        Bitmap[] b = {BitmapFactory.decodeResource(this.getResources(),
                R.drawable.green_dino)};
        Bitmap[] bitmap_values = new Bitmap[bitmap_keys.length];

        for (int i =0;i<bitmap_keys.length;i++){
            bitmap_values[i]=Bitmap.createScaledBitmap(b[i],
                    viewWidth / 2 + viewWidth / 20, viewHeight / 4, false);}
    }
}