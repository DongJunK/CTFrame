package com.example.adefault;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_Select extends AppCompatActivity {

    ImageView slide_img, photoEdit_img;
    TextView slide_txt, photoEdit_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_);

        slide_img = (ImageView)findViewById(R.id.slideshow);
        photoEdit_img = (ImageView)findViewById(R.id.photo_edit);

        slide_txt = (TextView)findViewById(R.id.textSlideShow);
        photoEdit_txt = (TextView)findViewById(R.id.textAlbumEdit);

        slide_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Select.this, Slideshow_Activity.class);
                startActivity(intent);
            }
        });

        photoEdit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Select.this,Activity_Login.class);
                startActivity(intent);
            }
        });

        slide_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Select.this, Slideshow_Activity.class);
                startActivity(intent);
            }
        });

        photoEdit_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Select.this,Activity_Login.class);
                startActivity(intent);
            }
        });
    }
}