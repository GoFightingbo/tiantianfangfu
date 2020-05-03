package com.xiangxue.myproject.ui;

import android.widget.ListView;

public class ImplantListView extends ListView {

    public ImplantListView(android.content.Context context,
                           android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 我们自己去测量 -- 解决显示不全问题
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { // 测量方法   计算
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
