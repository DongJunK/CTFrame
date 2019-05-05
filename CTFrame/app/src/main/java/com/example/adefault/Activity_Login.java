package com.example.adefault;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Login extends AppCompatActivity {
    Button btn_login;
    TextView tv_sign_up;
    EditText edt_email, edt_password;

    String Email;
    String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_sign_up = (TextView)findViewById(R.id.tv_sign_up);
        btn_login =(Button)findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Activity_Login.this , MainActivity.class);
                startActivity( intent );
            }
        });

    }

    //회원가입 클릭
    public void onClick_Sign_up(View view){
        Intent intent = new Intent( Activity_Login.this , Activity_Signup.class);
        startActivity( intent );
    }
}
