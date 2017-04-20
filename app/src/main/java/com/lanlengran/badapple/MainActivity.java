package com.lanlengran.badapple;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //7  0 0
    private ImageView imageView;
    private TextView textView;
    ScheduledExecutorService service = null;
    Runnable runnable;
    private Handler handler = new Handler();
    int imgNum = 0;
    private int mSkip,mGap;
    private MediaPlayer musicPlayer;
    private int[] colors;
    private int pointNum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imgview);
        textView = (TextView) findViewById(R.id.textview);
        mSkip=getIntent().getIntExtra("skip",2);
        mGap=getIntent().getIntExtra("gap",5);
        runnable = new Runnable() {
            public void run() {
                // task to run goes here
                System.out.println("Hello !!");
                String strNum = "" + imgNum;
                while (strNum.length() < 4) {
                    strNum = "0" + strNum;
                }
                Log.d("qin", "strNum==========>" + strNum);
                int resId1 = getResources().getIdentifier("img0" + strNum, "drawable", getPackageName());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId1);
                Image2String(bitmap);
                if (imgNum > 3110) {
                    service.shutdown();
                } else {
                    imgNum = imgNum + mSkip;
                }
            }
        };
        service = Executors
                .newSingleThreadScheduledExecutor();
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.scheduleAtFixedRate(runnable, 7000, 71 * mSkip, TimeUnit.MILLISECONDS);
        iniPlayer();
//        Log.d("qin","resId1"+resId1);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timg);
        Image2String(bitmap);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setTextColor(colors[pointNum%colors.length]);
                pointNum++;
              //  textView.setTextColor();
            }
        });

        colors=new int[]{getResources().getColor(android.R.color.black),getResources().getColor(android.R.color.holo_red_light),
                getResources().getColor(android.R.color.darker_gray),getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_dark)};
    }
    /**
     * 初始化音乐播放器
     */
    public void iniPlayer() {
        try {
            musicPlayer = new MediaPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            musicPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.badapple));
            musicPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicPlayer.setLooping(false);
        musicPlayer.start();


//        UCSCall.startCallRinging(AppManager.getInstance(),"dialling_tone.pcm");
//        UCSCall.startCallRinging(AppManager.getInstance(),"dialling_tone.pcm");
    }
    private void Image2String(final Bitmap bitmap) {
        //        clors=new int[bitmap.getHeight()*bitmap.getHeight()];
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        //  Bitmap bitmap2=Bitmap.createBitmap(pixels,0,bitmap.getWidth(),bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.RGB_565);

        final int gap = DensityUtils.dip2px(this, mGap);
        // int gap=80;
        Log.d("qin", "getWidth==========>" + bitmap.getWidth());
        Log.d("qin", "gap==========>" + gap);
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bitmap.getHeight() - gap; i = i + gap) {
            for (int j = 0; j < bitmap.getWidth() - gap; j = j + gap) {
                //  int color=pixels[(i+gap/2)*(j+gap/2)];
                int color = pixels[i * bitmap.getWidth() + j];
//                if (i==0){
//                    Log.d("qin","i*bitmap.getWidth()==========>"+i*bitmap.getWidth());
//                    Log.d("qin","j*gap========================>"+j*gap);
//                    Log.d("qin","size=========================>"+i*bitmap.getWidth()+j*gap);
//                }
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                if ((r + g + b) < 380) {
//                    for (int k=0;k<gap/2;k++){
//                        sb.append("蓝");
//                    }
                    sb.append("蓝");
                } else {
//                    for (int k=0;k<gap/2;k++){
//                        sb.append("  ");
//                    }
                    sb.append("。");
                }
            }
//            for (int k=0;k<DensityUtils.px2dip(this,gap);k++){
//                sb.append("\n");
//            }
            sb.append("\n");
        }
        //  Log.d("qin",sb.toString());
        Log.d("qin", sb.toString().length() + "");
        handler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
                textView.setText(sb.toString());
                textView.setTextSize(DensityUtils.px2dip(MainActivity.this, gap));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        service.shutdown();
        musicPlayer.stop();
        musicPlayer.release();
    }
}
