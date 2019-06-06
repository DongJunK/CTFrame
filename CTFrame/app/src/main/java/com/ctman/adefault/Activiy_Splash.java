package com.ctman.adefault;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Activiy_Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView iv = (ImageView)findViewById(R.id.imageView);
        //iv.setImageResource(R.drawable.splash);

        Glide.with(this).load(R.raw.splash).into(iv);
        //Glide.with(this).load(R.raw.splash).into(iv);
        //GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Activiy_Splash.this, Activity_Select.class);
                startActivity(intent);
                //Log.i("fail_reason", "실행 됨");
                finish();
            }
        }, 9000);
    }
}
