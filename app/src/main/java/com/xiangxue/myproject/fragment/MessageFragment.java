package com.xiangxue.myproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.xiangxue.myproject.R;

public class MessageFragment extends Fragment {

    // 创建View
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_message,null);
        return view;
    }
}
