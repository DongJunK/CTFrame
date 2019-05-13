package com.example.adefault;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adefault.Interfaces.SendDataToServer;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity_MyPage extends AppCompatActivity {

    EditText edt_email, edt_name, edt_favorite, edt_password;
    String name, favorite;
    Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        //리소스 매칭
        edt_email = (EditText)findViewById(R.id.edt_email);
        edt_name = (EditText)findViewById(R.id.edt_name);
        edt_favorite = (EditText)findViewById(R.id.edt_favorite);
        edt_password = (EditText)findViewById(R.id.edt_password);
        btn_logout = (Button)findViewById(R.id.btn_logout);
        MyPageRequest();

        edt_email.setText(MainActivity.loginId);
        edt_name.setText(name);


        //로그아웃
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Activity_MyPage.this , Activity_Login.class);
                startActivity( intent );
                Toast.makeText(Activity_MyPage.this,"로그아웃 되었습니다 .", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void MyPageRequest()
    {
        //section 0 여기 건들지마
        JSONObject obj = new JSONObject();
        SendDataToServer sendDataToServer = new SendDataToServer();
        //section 0 여기 건들지마

        //section 2 여기는 고치치마라//
        JSONObject post_dict = new JSONObject();
        //section 2 여기 까지//

        //section 3 보내야 하는 값 만큼 매치시켜줘서 보내면됨//
        try {
            post_dict.put("email" , MainActivity.loginId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //section 3 여기까지//

        if (post_dict.length() > 0) {
            try
            {
                //section 4   "signUpCheck 라고 되어있는 부분을 승배가 준 파일로 고쳐서 보낼것 //
                obj = new JSONObject(sendDataToServer.execute(String.valueOf(post_dict),"mypage_request").get());
                //section 4//

                try
                {
                    //sign_in 의 받을 값은 Header, responseMsg이다
                    //section 5 받을 값만 받으면 된다 ()안에 값은 서버랑 일치 시키도록해야함
                    name = obj.getString("name");
                    favorite = obj.getString("favorite");
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
    }
}
