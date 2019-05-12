package com.example.adefault;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adefault.Interfaces.SendDataToServer;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Login extends AppCompatActivity {
    Button btn_login;
    TextView tv_sign_up;
    EditText edt_email, edt_password;
    int responseMsg = 3; //sign_in 에 대한 return 값을 저장한다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //퍼미션 요청
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        //리소스 매칭
        tv_sign_up = (TextView)findViewById(R.id.tv_sign_up);
        btn_login =(Button)findViewById(R.id.btn_login);
        edt_email = (EditText)findViewById(R.id.edt_email);
        edt_password = (EditText)findViewById(R.id.edt_password);

        //리스너 등록
        //로그인 버튼 클릭
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginCheck(view);
                if(responseMsg == 1 )
                {
                    //로그인 성공
                    Toast.makeText(Activity_Login.this,"로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent( Activity_Login.this , MainActivity.class);
                    intent.putExtra("email", edt_email.getText().toString());
                    startActivity( intent );
                }
                else if(responseMsg == 2 )
                {
                    //비밀번호 틀림
                    Toast.makeText(Activity_Login.this,"비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //회원정보 없음
                    Toast.makeText(Activity_Login.this,"회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //회원가입 클릭
    public void onClick_Sign_up(View view){
        Intent intent = new Intent( Activity_Login.this , Activity_Signup.class);
        startActivity( intent );
    }

    public void LoginCheck(View v)
    {
        JSONObject obj = new JSONObject();
        SendDataToServer sendDataToServer = new SendDataToServer();

        JSONObject post_dict = new JSONObject();

        //section 3 보내야 하는 값 만큼 매치시켜줘서 보내면됨//
        try {
            post_dict.put("email" , edt_email.getText().toString());
            post_dict.put("pass", edt_password.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //section 3 여기까지//

        if (post_dict.length() > 0) {
            try
            {
                //section 4   "signUpCheck 라고 되어있는 부분을 승배가 준 파일로 고쳐서 보낼것 //
                obj = new JSONObject(sendDataToServer.execute(String.valueOf(post_dict),"sign_in").get());
                //section 4//

                try
                {
                    //sign_in 의 받을 값은 Header, responseMsg이다
                    //section 5 받을 값만 받으면 된다 ()안에 값은 서버랑 일치 시키도록해야함
                     responseMsg = obj.getInt("responseMsg");

                    //return responseMsg == 1 -> sign_in Success
                    //return responseMsg == 2 -> incorrect password
                    //return responseMsg == 3 -> not exist account

                    //section 5
                }
                catch (JSONException e)
                {
                    Log.i("JSONException", "JSONError : " + e.toString());
                }
            }
            catch (Exception e)
            {
                Log.i("Exception",e.toString());
            }
        }
        Log.i("returnMsg", responseMsg+"");
    }
}
