package com.example.adefault;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_mypage:
                    mTextMessage.setText("Mypage");
                    return true;
                case R.id.navigation_pixabay:
                    mTextMessage.setText(R.string.title_pixabay);
                    Intent intent = new Intent(MainActivity.this, Activity_Pixabay.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_googledrive:
                    mTextMessage.setText(R.string.title_googledrive);
                    return true;

                case R.id.navigation_mygallery:
                    mTextMessage.setText(R.string.title_mygallery);
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
