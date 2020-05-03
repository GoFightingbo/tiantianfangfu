package com.xiangxue.myproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.xiangxue.myproject.mode.UserLoginResult;
import com.xiangxue.myproject.util.CommUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserLoginActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private CheckBox mCheckPwdCb; //隐藏 显示 密码的 明文 密文
    private EditText mUserPhoneText, mUserPwdText; // 用户的手机号和密码
    private TextView mUserRegisterText; //跳转到注册
    private Button mUserLoginBt;  //登录按钮
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        mCheckPwdCb = findViewById(R.id.check_password_cb);
        mUserPhoneText = findViewById(R.id.user_phone_et);
        mUserPwdText = findViewById(R.id.user_password_et);
        mUserRegisterText = findViewById(R.id.user_register_tv);
        mUserLoginBt = findViewById(R.id.user_login_bt);

        //密码的显示checkbox监听
        mCheckPwdCb.setOnCheckedChangeListener(this);

        //点击登录按钮提交数据
        mUserLoginBt.setOnClickListener(this);

        //点击注册的text
        mUserRegisterText.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mUserPwdText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //隐藏密码
            mUserPwdText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        CommUtil.cursorToEnd(mUserPwdText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_login_bt:
                //首先终端验证一遍，确保输入的数据是符合要求的
                String userPhone = mUserPhoneText.getText().toString().trim();
                String userPwd = mUserPwdText.getText().toString().trim();
                if (TextUtils.isEmpty(userPhone)) {
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(userPwd)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
                    return;
                }
                requestServerLogin(userPhone, userPwd);
                break;
            case R.id.user_register_tv :
                Intent registerIntent = new Intent(this, UserRegisterActivity.class);
                startActivity(registerIntent);
                break;
        }
    }

    /**
     * 请求服务器去登陆
     */
    private void requestServerLogin(String userPhone, String userPwd) {
        //okHttp请求服务器
        OkHttpClient okHttpClient = new OkHttpClient();
        //2以Form表单的形式去请求
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //3封装参数
        builder.addFormDataPart("appid", "1");
        builder.addFormDataPart("cell_phone", userPhone);
        builder.addFormDataPart("password", userPwd);

        //请求服务器的URL
        final String LOGIN_URL = "http://v2.ffu365.com/index.php?m=Api&c=Member&a=login";

        //构建请求对象
        Request request = new Request.Builder().url(LOGIN_URL).post(builder.build()).build();

        //开始请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功后返回的数据在response中，我们需要把后台返回的字符串拿到
                String loginResult = response.body().string();
                Log.d("wangbo", "登录返回的数据loginResult: " + loginResult);
                //服务器的数据映射成bean
                Gson gson = new Gson();
                final UserLoginResult userLoginResult = gson.fromJson(loginResult, UserLoginResult.class);

                //拿到数据后去刷新页面
                mHandler.post(new Runnable() { //显示数据是异步的， 需要切换到主线程
                    @Override
                    public void run() {
                        dealLoginResult(userLoginResult);
                    }
                });
            }
        });
    }

    /**
     *okHttp请求分为以下几个步骤
     * 1. 首先创建一个okHttpClient
     * 2. 确认用哪种形式去请求，Form表单 或者其他形式
     * 3. 封装参数，把我们需要穿进去的参数放到表单中
     * 4. 构建Request， 其中包含表单builder 和 服务器地址url
     * 5. 发起请求
     */

    //处理请求后，服务器返回的数据
    private void dealLoginResult(UserLoginResult userLoginResult) {
        //首先判断请求是否成功
        if (userLoginResult.getErrcode() == 1) {
            //1. 需要保存登录的状态，当前设置为已经登录
            SharedPreferences sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("is_login", true).commit();

            //2. 保存用户信息
            UserLoginResult.DataBean userData = userLoginResult.getData();
            //SharedPerferences 如何保存对象， 怎么把对象转换为json  String -> SharedPreferences
            Gson gson = new Gson();
            String userInfoStr = gson.toJson(userData);
            //保存用户信息为json字符串格式
            sharedPreferences.edit().putString("user_info", userInfoStr).commit();

            //关闭这个页面
            finish();
        } else {
            //登录失败
            Toast.makeText(this, userLoginResult.getErrmsg(), Toast.LENGTH_LONG).show();
        }
    }
}
