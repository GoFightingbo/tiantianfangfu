package com.xiangxue.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView welcome_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcome_iv = findViewById(R.id.welcome_iv);

        //主线程阻塞会报错，不能在UI线程处理
        //必须开启子线程

        //停顿5s 再跳转页面---》Main
        //1.sleep 2handler发送延迟消息  3.Timer类  4.用动画 0.7-0.1

        //4用动画 "alpha"的动画效果是透明的， 从0.7f 到 0.1f
        ObjectAnimator animator = new ObjectAnimator().ofFloat(R.id.welcome_iv, "alpha", 0.7f, 0.1f);

        //设置动画的执行时间
        animator.setDuration(5000);

        //开启动画
        animator.start();

        //监听动画什么时候完成， 等待动画执行完毕需要调用到主页面
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                //等待动画执行完毕，进入主页面
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
