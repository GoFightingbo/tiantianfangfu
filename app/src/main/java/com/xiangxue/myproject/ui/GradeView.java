package com.xiangxue.myproject.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.xiangxue.myproject.R;
import com.xiangxue.myproject.config.AppConfig;

/**
 * extends View：ImageView Button TextView ... 没有包含子控件  【测量】  【绘制】
 * extends ViewGroup：LinearLayout ... 包含子控件  【测量】 【排版】
 */

/**
 * ViewGroup：LinearLayout
 * View：ImageView Button TextView
 */

/**
 * 评分控件
 */
public class GradeView extends View {
    public GradeView(Context context) {
        // super(context);
        this(context, null);
    }

    public GradeView(Context context, @Nullable AttributeSet attrs) {
        // super(context, attrs);
        this(context, attrs, 0);
    }

    public GradeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 最终一定会调用到这个构造
        initView();
    }

    private Bitmap mOkBitmap; // 亮起来的
    private Bitmap mNonBitmap; // 灰色的
    private int mSimpleWidth = 0; // Bitmap宽度
    private static final int IMG_COUNT = 5;
    private int mCurrentValue = 0; // 当前 默认 0

    private void initView() {
        // 两张图片
        mOkBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bigger_grade_fcous);
        mNonBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bigger_grade_normal);

        // 只有宽度就够了，没有高度
        mSimpleWidth = mOkBitmap.getWidth();
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 得到计算后的 宽度 和 高度  +  padding
        int w = mSimpleWidth * IMG_COUNT + getPaddingLeft() + getPaddingRight();
        int h = mOkBitmap.getHeight() + getPaddingTop() + getPaddingBottom();

        // 测量模式   wrap_content   （MeasureSpec.EXACTLY == 200dp）   match_parent
        // 得到模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {

            // 防止用户给的不够宽
            int userWidth = MeasureSpec.getSize(widthMeasureSpec);
            if (userWidth < w) {
                throw new IllegalStateException("宽度不够....");
            }
            w = userWidth; // 更新用户的宽度
        }
        // 最后一步，设置我们测量好的 宽 和 高
        setMeasuredDimension(w, h);
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < IMG_COUNT; i++) {
            if (i < mCurrentValue) { // 就归零
                canvas.drawBitmap(mOkBitmap, i * mSimpleWidth, 0, null); // 亮起来的星星
            } else {
                canvas.drawBitmap(mNonBitmap, i * mSimpleWidth, 0, null);
            }
        }
    }

    // 触摸的事件

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // return super.onTouchEvent(event); // 如果是false，不会执行下去

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // 1.获取手指的位置
                int moveX = (int) event.getX();
                if (moveX <=0) {
                    mCurrentValue = -1;
                } else {
                    mCurrentValue = moveX / mSimpleWidth + 1;
                }
                Log.d(AppConfig.COMMON_TAG, "用户使用了评分控件 当前分数值:" + mCurrentValue);
                break;
        }

        // 修改此 mCurrentValue 值后，让重新绘制
        invalidate(); // 走系统流程 一定会调用 onDraw方法

        return true; // 如果这个地方返回true 代表会不断的执行下去
    }

    /**
     * 对外暴露当前的分数值
     * @return
     */
    public int getScore() {
        return mCurrentValue;
    }
}
