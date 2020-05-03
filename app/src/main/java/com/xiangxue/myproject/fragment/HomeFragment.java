package com.xiangxue.myproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.xiangxue.myproject.DetailLinkActivity;
import com.xiangxue.myproject.adapter.HomeInfoListAdapter;
import com.xiangxue.myproject.config.AppConfig;
import com.xiangxue.myproject.mode.HomeDataResult;
import com.xiangxue.myproject.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment  implements View.OnClickListener {

    private ImageView mAdbannerIv, mRecommendedCompanyIv;
    private View mRootView;
    private Context mContext;
    private Handler mHandler = new Handler();
    private ListView mNewsLv;

    private HomeDataResult mHomeDataResult;

    // View 相关的
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home, null);
        this.mContext = getActivity();
        return mRootView;
    }

    // 逻辑相关的
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdbannerIv = (ImageView) mRootView.findViewById(R.id.adbanner_iv);
        mRecommendedCompanyIv = (ImageView) mRootView.findViewById(R.id.recommended_company);
        mNewsLv = (ListView) mRootView.findViewById(R.id.industry_information_lv);

        // 请求服务器，服务器响应后台数据
        Log.d(AppConfig.COMMON_TAG, "HomeFragment onActivivtyCreated");
        requestHomeData();

        // 设置图片点击事件
        mAdbannerIv.setOnClickListener(this);
        mRecommendedCompanyIv.setOnClickListener(this);
    }

    /**
     * 请求后台数据
     */
    private void requestHomeData() {
        Log.d(AppConfig.COMMON_TAG, "HomeFragment requestHomeData");
        // 1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        // 2.构建参数的body  MultipartBody.FORM 表单形式
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        // 用户名，密码，点击按钮 请求服务器  表单提交
        // 2.2封装参数，可以理解是：用户名和密码
        builder.addFormDataPart("appid", "1");

        // 3.构建请求 请求服务器
        Request request = new Request.Builder().url("http://v2.ffu365.com/index.php?m=Api&c=Index&a=home")
                .post(builder.build()).build();

        // 4.开始发请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败
                Log.d(AppConfig.COMMON_TAG, "onFailure: 请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功
                String result = response.body().string();
                Log.d(AppConfig.COMMON_TAG, "onResponse: 请求成功 结果:" + result);

                // Gson 解析 服务器响应的 数据
                Gson gson = new Gson();
                mHomeDataResult = gson.fromJson(result, HomeDataResult.class);

                showHomeData(mHomeDataResult);

            }
        }); // 请求服务器是耗时操作，必须使用异步的 （线程池干活）
    }

    /**
     * 显示数据
     * @param mHomeDataResult
     */
    private void showHomeData(final HomeDataResult mHomeDataResult) { // 当前属于异步
        // 直接显示有问题，会奔溃

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                // TODO 第一张图片
                // main thread  可以 ui
                String adBannerImage = mHomeDataResult.getData().getAd_list().get(0).getImage();
                // 加载图片 Glide 框架
                Glide.with(mContext)
                        .load(adBannerImage) // 要加载那张图片，图片地址告诉
                        .into(mAdbannerIv);


                // TODO 第二张图片
                Glide.with(mContext)
                        .load(mHomeDataResult.getData().getCompany_list().get(0).getImage())
                        .into(mRecommendedCompanyIv);

                // TODO 把服务器数据，展示到 ListView.adapter 里面去
                mNewsLv.setAdapter(new HomeInfoListAdapter(mContext, mHomeDataResult.getData().getNews_list()));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.adbanner_iv:
                Intent intent = new Intent(mContext, DetailLinkActivity.class);

                // 获取广告位的链接 服务器给的
                String bannerUrl = mHomeDataResult.getData().getAd_list().get(0).getLink();

                // 把链接传递给下一个activity
                intent.putExtra(DetailLinkActivity.URL_KEY, bannerUrl);

                startActivity(intent);

                break;
            case R.id.recommended_company:
                Intent intent2 = new Intent(mContext, DetailLinkActivity.class);

                // 获取广告位的链接 服务器给的
                String bannerUrl2 = mHomeDataResult.getData().getCompany_list().get(0).getLink();

                // 把链接传递给下一个activity
                intent2.putExtra(DetailLinkActivity.URL_KEY, bannerUrl2);

                startActivity(intent2);
                break;
        }
    }
}