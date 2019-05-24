package com.example.adefault;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Select extends AppCompatActivity {

    private final int INTERNET_PERMISSION_CODE = 1;
    ImageView slide_img, photoEdit_img;
    TextView slide_txt, photoEdit_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_);

        slide_img = (ImageView) findViewById(R.id.slideshow);
        photoEdit_img = (ImageView) findViewById(R.id.photo_edit);

        slide_txt = (TextView) findViewById(R.id.textSlideShow);
        photoEdit_txt = (TextView) findViewById(R.id.textAlbumEdit);


        requestInternetPermission();
        slide_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Select.this, UnityPlayerActivity.class);
                startActivity(intent);
            }
        });

        photoEdit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Select.this, Activity_Login.class);
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
                Intent intent = new Intent(Activity_Select.this, Activity_Login.class);
                startActivity(intent);
            }
        });
    }

    private void requestInternetPermission() {

          new AlertDialog.Builder(this)
                  .setTitle("허용이 필요합니다")
                  .setMessage("인터넷을 사용하기 위해 허용이 필요합니다.")
                  .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          ActivityCompat.requestPermissions(Activity_Select.this,
                                  new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION_CODE);
                      }
                  })
                  .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                      }
                  })
                  .create().show();
      }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == INTERNET_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "인터넷 사용이 허용되었습니다", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "인터넷 사용이 거절되었습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

}