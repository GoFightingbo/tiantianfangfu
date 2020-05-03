package com.xiangxue.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.xiangxue.myproject.adapter.HomePagerAdapter;
import com.xiangxue.myproject.fragment.CenterFragment;
import com.xiangxue.myproject.fragment.CollectionFragment;
import com.xiangxue.myproject.fragment.HomeFragment;
import com.xiangxue.myproject.fragment.MessageFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private RadioButton mHomeRb, mCollectionRb, mMessageRb, mCenterRb;
    private ViewPager mViewPager;

    private final String TAG = "#######################";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("wangbo", "My First xiangxue app");

        //主页面的解决方案
        //1.ViewPager + FrageMent + RadioGroup(RadioButton) RadioButton是依赖与RadioButton的 目前是最好的
        //2.TabHost + Framgment 过时了
        //3.ViewGroup + Fragment + 动态的去切换， 会不断的销毁和创建（性能不好）

        //面向对象思想
        initView();
        initDate();
    }


    private final void initView() {
        mViewPager = (ViewPager)findViewById(R.id.view_pager);

        mHomeRb = (RadioButton)findViewById(R.id.home_rb);
        mCollectionRb = (RadioButton)findViewById(R.id.collection_rb);
        mMessageRb = (RadioButton)findViewById(R.id.message_rb);
        mCenterRb = (RadioButton)findViewById(R.id.center_rb);

        mHomeRb.setOnClickListener(this);
        mCollectionRb.setOnClickListener(this);
        mMessageRb.setOnClickListener(this);
        mCenterRb.setOnClickListener(this);

        mViewPager.setOnPageChangeListener(this);

    }


    private final void initDate() {
        //给viewpager设置adapter
        ArrayList<Fragment> fragments = new ArrayList<>();
        // 1.往集合里面添加Fragment
        fragments.add(new HomeFragment());
        fragments.add(new CollectionFragment());
        fragments.add(new MessageFragment());
        fragments.add(new CenterFragment());

        //适配器
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), fragments);


        //适配器交给viewPager
        mViewPager.setAdapter(homePagerAdapter);
    }

    //每次点击需要做事情的地方
    @Override
    public void onClick(View v) {
        Log.d(TAG, "button is click....!!!!!!!!!!!!!................");
        switch (v.getId()) {
            case R.id.home_rb:
                //把view切换到第一页
                Log.d(TAG, "home button is click....................");
                mViewPager.setCurrentItem(0, false);// false 代表切换的时候不显示滑动效果
                break;
                //把view切换到第二页
            case R.id.collection_rb:
                mViewPager.setCurrentItem(1, false);
                break;
            //把view切换到第三页
            case R.id.message_rb:
                mViewPager.setCurrentItem(2, false);
                break;
            //把view切换到第四页
            case R.id.center_rb:
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    /**
     * position 0 菜单一 ， 1 菜单二   ....
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        // 改变四个菜单的效果, 根据viewpager的滑动来改变下面button的状态，因为他们都是相关联的
        // 切换到相应页面之后调用  postion 当前的位置
        Log.d(TAG, "onPageSelected---------------------");
        switch(position){
            case 0:
                mHomeRb.setChecked(true);
                break;
            case 1:
                mCollectionRb.setChecked(true);
                break;
            case 2:
                mMessageRb.setChecked(true);
                break;
            case 3:
                mCenterRb.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
