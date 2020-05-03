package com.xiangxue.myproject;

import android.content.SharedPreferences;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
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
import com.xiangxue.myproject.mode.UserRequestCodeResult;
import com.xiangxue.myproject.ui.VerificationCodeButton;
import com.xiangxue.myproject.util.ActivityManagerUtil;
import com.xiangxue.myproject.util.CommUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserRegisterActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private CheckBox mPwdCheckBox;
    private EditText mUserPhoneTx, mUserPwdTx, mVerifyCodeTx;
    private Button mRegisterBt;
    private TextView mUserAgreementTx;
    private VerificationCodeButton mSendCodeBt;

    //切换主线程
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        mPwdCheckBox = findViewById(R.id.check_password_cb);
        mUserPhoneTx = findViewById(R.id.user_phone_et);
        mUserPwdTx = findViewById(R.id.user_password_et);
        mVerifyCodeTx = findViewById(R.id.user_code_et);
        mRegisterBt = findViewById(R.id.user_register_bt);
        mUserAgreementTx = findViewById(R.id.user_agreement_tv);
        mSendCodeBt = findViewById(R.id.send_code_bt);

        //密码显示的checkbox监听
        mPwdCheckBox.setOnCheckedChangeListener(this);

        //验证码提交
        mSendCodeBt.setOnClickListener(this);
        mRegisterBt.setOnClickListener(this);

        // 专业协议
        mUserAgreementTx.setText(Html.fromHtml("我已阅读并同意<font color='#24cfa2'>《享学咨询》</font>"));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mUserPwdTx.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            mUserPwdTx.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        CommUtil.cursorToEnd(mUserPwdTx);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_register_bt:
                dealUserRegister();
                break;
            case R.id.send_code_bt:
                mSendCodeBt.startLoad();

                //请求服务器
                requestUserCode();
                break;
        }
    }

    private void requestUserCode() {
        //终端校验 获取验证码
        String userPhone = mUserPhoneTx.getText().toString().trim();
        if (TextUtils.isEmpty(userPhone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG).show();
        }

        //2. 往后台提交数据
        OkHttpClient okHttpClient = new OkHttpClient();
        //3. 构建请求数据的类型， Form表单的形式
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //3.2封装参数
        builder.addFormDataPart("appid", "1");
        builder.addFormDataPart("cell_phone", userPhone);
        builder.addFormDataPart("sms_type", "3");

        final String REGISTER_URL = "http://v2.ffu365.com/index.php?m=Api&c=Util&a=sendVerifyCode";

        //4. 构建请求体
        Request request = new Request.Builder().url(REGISTER_URL).post(builder.build()).build();

        //5. 发起请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功更新数据
                String result = response.body().string();
                Log.d("wangbo", "验证码请求结果 result:" + result);
                Gson gson = new Gson();
                UserRequestCodeResult userRequestCodeResult = gson.fromJson(result, UserRequestCodeResult.class);
                //更新UI
                dealCodeResult(userRequestCodeResult);
            }
        });
    }

    private void dealCodeResult(final UserRequestCodeResult userRequestCodeResult) {
        //更新UI需要切换到主线程
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (userRequestCodeResult.errcode == 1) {
                    mSendCodeBt.aginAfterTime(60); //让用户等待60秒
                } else {
                    Toast.makeText(UserRegisterActivity.this, "验证码是：" + userRequestCodeResult.errmsg,
                            Toast.LENGTH_LONG).show();
                    mSendCodeBt.setNoraml(); //恢复验证码按钮状态
                }
            }
        });
    }

    //注册按钮点击后的操作
    private void dealUserRegister() {
        //验证终端的数据是否为空
        String userPhone = mUserPhoneTx.getText().toString().trim();
        String codeVerification = mVerifyCodeTx.getText().toString().trim();
        String userPwd = mUserPwdTx.getText().toString().trim();
        if (TextUtils.isEmpty(userPhone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(codeVerification)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(userPwd)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
            return;
        }

        //1.创建okHttpClient
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建请求数据类型
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("appid", "1");
        builder.addFormDataPart("cell_phone", userPhone);
        builder.addFormDataPart("verify_code", codeVerification);
        builder.addFormDataPart("password", userPwd); // MD5 AES 密码

        //构建请求体
        final String REGISTER_URL = "http://v2.ffu365.com/index.php?m=Api&c=Member&a=register";
        Request request = new Request.Builder().url(REGISTER_URL).post(builder.build()).build();

        //开始请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功， 接收返回的服务器数据
                String result = response.body().string();
                Gson gson = new Gson();
                final UserLoginResult userLoginResult = gson.fromJson(result, UserLoginResult.class);
                //将数据更新到UI,需要切换到主线程
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dealRegisterResult(userLoginResult);        //切换回去主线程 处理UI

                    }
                });
            }
        });
    }

    private void dealRegisterResult(final UserLoginResult userLoginResult) {
        if (userLoginResult.getErrcode() == 1) {
            //成功处理需要保存登录状态
            //1. 保存登录状态， 设置为已登录
            SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
            sp.edit().putBoolean("is_login", true).commit();
            //2. 保存用户信息
            UserLoginResult.DataBean userData = userLoginResult.getData();
            //SharedPreference 如何保存对象， 把对象转换为JSON String  -》SharedPreference
            Gson gson = new Gson();
            String userInfo = gson.toJson(userData);
            //将用户的信息已json的形式保存
            sp.edit().putString("user_info", userInfo).commit();

            //关闭当前页面，并且关闭掉登录页面
            //模拟注册成功，先关闭当前页面，再关闭登录页面
            ActivityManagerUtil.getInstance().finishActivity(this);
            ActivityManagerUtil.getInstance().finishActivity(UserLoginActivity.class);
        } else {
            Toast.makeText(this, userLoginResult.getErrmsg(), Toast.LENGTH_LONG).show();
        }
    }
}
