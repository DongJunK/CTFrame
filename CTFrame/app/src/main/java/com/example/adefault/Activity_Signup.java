package com.example.adefault;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adefault.Interfaces.SendDataToServer;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Signup extends AppCompatActivity {

    EditText edt_email, edt_email_code, edt_password, edt_password_confirm, edt_name;
    Button btn_email_check, btn_email_code, btn_sign_up;
    String authenticode;        //서버로 부터 받을 authenticode
    boolean isCheck, isEmailCheck;        //email 인증이버튼이 눌렸는지 여부
    int responseMsg;        //회원가입 성공여부
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //리소스 매칭
        edt_email = (EditText)findViewById(R.id.edt_email);
        edt_email_code = (EditText)findViewById(R.id.edt_email_code);
        edt_password = (EditText)findViewById(R.id.edt_password);
        edt_password_confirm = (EditText)findViewById(R.id.edt_password_confirm);
        edt_name = (EditText)findViewById(R.id.edt_name);

        btn_email_check = (Button)findViewById(R.id.btn_email_check);
        btn_email_code = (Button)findViewById(R.id.btn_email_code);
        btn_sign_up = (Button)findViewById(R.id.btn_sign_up);

        isEmailCheck = false;
        isCheck = false;

        //클릭 리스너 등록
        btn_email_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_email.getText().toString().equals(""))
                {
                    Toast.makeText(Activity_Signup.this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //email로 인증코드 발생
                    EmailCheck(view);
                    isCheck = true;
                    Toast.makeText(Activity_Signup.this, "이메일로 인증코드를 발송하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_email_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCheck)
                {
                    Log.i("authenticode", "autenticode :: " + authenticode);
                    //해당 인증코드 확인
                    if(edt_email_code.getText().toString().equals(authenticode))
                    {
                        //인증코드가 맞다면
                        //인증코드 버튼 제거 & email, email_code부분 block
                        edt_email.setEnabled(false);
                        edt_email_code.setEnabled(false);
                        btn_email_check.setEnabled(false);
                        btn_email_code.setEnabled(false);

                        isEmailCheck = true;
                        Toast.makeText(Activity_Signup.this, "이메일 인증을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Activity_Signup.this, "이메일 인증 코드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Activity_Signup.this, "이메일 인증 코드를 발송하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmailCheck)
                {
                    //이메일인증이 완료되었음
                    if(edt_password.getText().toString().equals(""))
                    {
                        Toast.makeText(Activity_Signup.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                    else if(edt_password_confirm.getText().toString().equals(""))
                    {
                        Toast.makeText(Activity_Signup.this, "비밀번호 확인란을 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                    else if(!edt_password.getText().toString().equals(edt_password_confirm.getText().toString()))
                    {
                        //입력한 password가 다르다면
                        Toast.makeText(Activity_Signup.this, "비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                    }
                    else if(edt_name.getText().toString().equals(""))
                    {
                        Toast.makeText(Activity_Signup.this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //회원가입 완료
                        btn_sign_up.setEnabled(false);
                        SignUp(view);
                        if(responseMsg == 1)
                        {
                            //회원가입 성공하면 픽사베이 자동 다운
                            downpixa(view);
                            Toast.makeText(Activity_Signup.this, "회원가입을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(Activity_Signup.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(Activity_Signup.this, "이메일 인증을 완료하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void EmailCheck(View v)
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
            post_dict.put("email" , edt_email.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //section 3 여기까지//

        if (post_dict.length() > 0) {
            try
            {
                //section 4   "signUpCheck 라고 되어있는 부분을 승배가 준 파일로 고쳐서 보낼것 //
                obj = new JSONObject(sendDataToServer.execute(String.valueOf(post_dict),"email_check").get());
                //section 4//

                try
                {
                    //sign_in 의 받을 값은 Header, responseMsg이다
                    //section 5 받을 값만 받으면 된다 ()안에 값은 서버랑 일치 시키도록해야함
                    authenticode = obj.getString("responseMsg");
                    Log.i("authenticode", "autenticode :: " + authenticode);
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

    public void SignUp(View v)
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
            post_dict.put("email" , edt_email.getText().toString());
            post_dict.put("pass" , edt_password.getText().toString());
            post_dict.put("name" , edt_name.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //section 3 여기까지//

        if (post_dict.length() > 0) {
            try
            {
                //section 4   "signUpCheck 라고 되어있는 부분을 승배가 준 파일로 고쳐서 보낼것 //
                obj = new JSONObject(sendDataToServer.execute(String.valueOf(post_dict),"sign_up").get());
                //section 4//

                try
                {
                    //sign_in 의 받을 값은 Header, responseMsg이다
                    //section 5 받을 값만 받으면 된다 ()안에 값은 서버랑 일치 시키도록해야함
                    responseMsg = obj.getInt("responseMsg");
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

    public void downpixa(View v)
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
            post_dict.put("email" , edt_email.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //section 3 여기까지//

        if (post_dict.length() > 0) {
            try
            {
                //section 4   "signUpCheck 라고 되어있는 부분을 승배가 준 파일로 고쳐서 보낼것 //
                obj = new JSONObject(sendDataToServer.execute(String.valueOf(post_dict),"pixa_downimage").get());
                //section 4//

                try
                {
                    //sign_in 의 받을 값은 Header, responseMsg이다
                    //section 5 받을 값만 받으면 된다 ()안에 값은 서버랑 일치 시키도록해야함
                    responseMsg = obj.getInt("responseMsg");
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
