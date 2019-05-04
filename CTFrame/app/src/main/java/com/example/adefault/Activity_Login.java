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
    Button login_Btn;
    TextView forgot_pw_Tv, sign_up_Tv;
    Button btn_sign_in;
    EditText signin_name, signin_password;

    String Email;
    String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgot_pw_Tv = (TextView)findViewById(R.id.forgot_pw_Tv);
        sign_up_Tv = (TextView)findViewById(R.id.sign_up_Tv);
        login_Btn =(Button)findViewById(R.id.login_Btn);

        //로그인 동작
        ((Button) login_Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Activity_Login.this , MainActivity.class);
                startActivity( intent );

            }
                /*
                String userID = signin_name.getText().toString();
                String userPassword = signin_password.getText().toString();
                if(userID.equals(""))
                {
                    Toast.makeText(Activity_Login.this, "please fill in the e-mail", Toast.LENGTH_SHORT).show();
                    signin_name.requestFocus();
                }
                else if(userPassword.equals(""))
                {
                    Toast.makeText(Activity_Login.this, "please fill in the password", Toast.LENGTH_SHORT).show();
                    signin_password.requestFocus();
                }
                else {
                    //SendDataToServer 사용하는 구간
                    SendDataToServerSignInCheck(v);
                    //SendDataToServer사용하는 구간 여기까지
                    if(responseCode == 1)
                    {
                        //로그인 성공
                        Toast.makeText(Activity_Login.this, "SignIn!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_Login.this, MainActivity.class);
                        //이 Activity에서 MainActivity로 값을 넘겨줌
                        FirstscreenActivity.seq_num = seq_num;
                        startActivity(intent);
                    }
                    else
                    {
                        //로그인 실패
                        Toast.makeText(Activity_Login.this, "Incorrect User Information", Toast.LENGTH_SHORT).show();
                    }

                }
            }
             */
        });

    }

    //회원가입 클릭
    public void onClick_Sign_up(View view){
        Intent intent = new Intent( Activity_Login.this , Activity_Signup.class);
        startActivity( intent );
    }
}
