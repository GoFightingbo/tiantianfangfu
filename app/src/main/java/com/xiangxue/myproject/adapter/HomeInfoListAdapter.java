package com.xiangxue.myproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiangxue.myproject.R;
import com.xiangxue.myproject.config.AppConfig;
import com.xiangxue.myproject.mode.HomeDataResult;

import java.util.List;

// ListView 适配器
public class HomeInfoListAdapter extends BaseAdapter {

    private List<HomeDataResult.DataBean.NewsListBean> mNews; // 数据对象
    private Context mContext; // 环境，为什么要环境，因为需要加载 item

    public HomeInfoListAdapter(Context context, List<HomeDataResult.DataBean.NewsListBean> news){
        this.mContext = context;
        this.mNews = news;
    }

    @Override
    public int getCount() {
        return mNews.size();
    }

    @Override
    public Object getItem(int position) {
        return mNews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView =  View.inflate(mContext, R.layout.item_industry_information_list,null);

            viewHolder = new ViewHolder();
            viewHolder.newsText = (TextView) convertView.findViewById(R.id.news_text);
            viewHolder.newDate = (TextView) convertView.findViewById(R.id.news_date);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 设置条目数据
        final HomeDataResult.DataBean.NewsListBean news = mNews.get(position);
        Log.d(AppConfig.COMMON_TAG, "TITLE= " + news.getTitle());
        viewHolder.newsText.setText(news.getTitle());
        viewHolder.newDate.setText(news.getCreate_time());
        return convertView;
    }


    public static class ViewHolder{
        public TextView newsText;
        public TextView newDate;
    }
}