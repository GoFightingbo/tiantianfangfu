package com.xiangxue.myproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xiangxue.myproject.R;
import com.xiangxue.myproject.UserLoginActivity;

public class CenterFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private TextView loginTextView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_center, null);
        loginTextView = view.findViewById(R.id.user_login_tv);
        loginTextView.setOnClickListener(this);
        return view;
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
        }
    }
}
