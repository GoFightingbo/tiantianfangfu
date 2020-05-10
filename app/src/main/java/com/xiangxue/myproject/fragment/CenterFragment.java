package com.xiangxue.myproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.xiangxue.myproject.R;
import com.xiangxue.myproject.UserInfoActivity;
import com.xiangxue.myproject.UserLoginActivity;
import com.xiangxue.myproject.mode.UserLoginResult;
import com.xiangxue.myproject.ui.RoundImageView;

public class CenterFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private TextView loginTextView;
    private TextView mUserLoginTv,mUserExitLoginTv,mUserNameTv,mUserLocationTv;
    private LinearLayout mUserLoginedLl,mRechargeCoinLl;
    private ImageView mUserHeadIv;
    private View mRootView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_center, null);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // findViewById
        mUserLoginTv = (TextView) mRootView.findViewById(R.id.user_login_tv);
        mUserExitLoginTv = (TextView) mRootView.findViewById(R.id.user_exit_login);
        mUserLoginedLl = (LinearLayout) mRootView.findViewById(R.id.user_logined_ll);
        mUserHeadIv = (ImageView) mRootView.findViewById(R.id.user_head_iv);
        mUserNameTv = (TextView) mRootView.findViewById(R.id.user_name_tv);
        mUserLocationTv = (TextView) mRootView.findViewById(R.id.user_location_tv);
        mRechargeCoinLl = (LinearLayout) mRootView.findViewById(R.id.recharge_coin_ll);

        mUserLoginTv.setOnClickListener(this);
        mUserExitLoginTv.setOnClickListener(this);
        mUserLoginedLl.setOnClickListener(this);
        mRechargeCoinLl.setOnClickListener(this);

        loginTextView = mRootView.findViewById(R.id.user_login_tv);
        loginTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_login_tv:
                Log.d("wangbo", "登录注册text被点击了");
                //跳转到登录界面
                Intent loginIntent = new Intent(mContext, UserLoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.user_logined_ll:
                //跳转到用户详情
                Intent userInfoIntent = new Intent(mContext, UserInfoActivity.class);
                startActivity(userInfoIntent);
                break;
        }
    }

    @Override
    public void onResume() { //登录成功后 会调用这个函数
        super.onResume();
        //首先会判断用户是否登录， 如果登录了，显示登录的中心头部， 否则显示未登录的中心头部
        SharedPreferences sp = mContext.getSharedPreferences("info", Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("is_login", false);
        if (isLogin) {
            mUserLoginedLl.setVisibility(View.VISIBLE);
            mUserLoginTv.setVisibility(View.GONE);

            //设置到用户信息
            String userInfoStr = sp.getString("user_info", null);
            Log.d("Derry", "userInfoStr: " + userInfoStr);

            //取出登录成功的数据， 把数据取出来  显示圆形头像
            if (!TextUtils.isEmpty(userInfoStr)) {
                //把用户信息JSON转为对象
                Gson gson = new Gson();
                UserLoginResult.DataBean userInfo = gson.fromJson(userInfoStr, UserLoginResult.DataBean.class);

                Log.d("Derry", "getMember_avatar: " + userInfo.getMember_info().getMember_avatar());

                //TODO 圆角图片， 以后解决
                //显示图片
                Glide.with(mContext).load(userInfo.getMember_info().getMember_avatar()).into(mUserHeadIv);

                //设置名称和地区
                mUserNameTv.setText(userInfo.getMember_info().getMember_name());
                mUserLocationTv.setText(userInfo.getMember_info().getMember_location_text());
            }
        } else {
            //没有登录
        }
    }
}
