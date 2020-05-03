package com.xiangxue.myproject.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.xiangxue.myproject.R;
import com.xiangxue.myproject.config.AppConfig;

/**
 * View：ImageView Button  TextView ... 没有包含子控件  【测量】  【绘制】
 * ViewGroup：LinearLayout ... 包含子控件  【测量】 【排版】
 */

@SuppressLint("AppCompatCustomView")
public class HomeImageView extends ImageView {

    /**
     * Java new HomeImageView(this)
     * @param context
     */
    public HomeImageView(Context context) {
        // super(context);
        this(context, null);
    }

    /**
     * 布局里面 指定关联的
     * @param context
     * @param attrs
     */
    public HomeImageView(Context context, @Nullable AttributeSet attrs) {
        // super(context, attrs);
        this(context, attrs, 0);
    }

    /**
     * 布局里面 指定关联的 加了style的
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public HomeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 构造最终是走到这里来
        initView(context, attrs);
    }

    private float mWidth;
    private float mHeight;

    // 初始化
    // 获取 用户设置的值  得到自定义属性
    private void initView(Context context, AttributeSet attrs) {
        // 获取 属性 的 数组
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HomeImageView);

        // 取出用户设置的值
        mWidth = array.getFloat(R.styleable.HomeImageView_width_home, 0);
        mHeight = array.getFloat(R.styleable.HomeImageView_height_home, 0);

        Log.d(AppConfig.COMMON_TAG, "initView: mWidth:" + mWidth + " mHeight:" + mHeight);

        // 一定要释放
        array.recycle();
    }

    /**
     * 测量的函数  测量宽和高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 我们自己测量的时候，对高度进行处理

        // 1.获取宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);

        // 2.获取高度计算,  例如：2/1  or   3/1  等等，动态设置
        int height = (int) (width * mHeight/mWidth);

        // 3.设置进去 改变了
        setMeasuredDimension(width, height);

    }

    /**
     * 没有干活
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
