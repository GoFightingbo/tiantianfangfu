package com.xiangxue.myproject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// 用户的详情
public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ALBUM_OK = 3432;
    private static final int CUT_OK = 0x0013;

    private ImageView mUserLogoIV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mUserLogoIV = findViewById(R.id.user_logo);
        mUserLogoIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 弹出选择照片或是拍照的 dialog
        showDialog();
    }

    /**
     * 弹出一个弹出框
     */
    private void showDialog() {
        final Dialog dialog = new Dialog(this, R.style.dialog);

        // 自定义布局 View
        View dialogView = View.inflate(this, R.layout.photo_choose_dialog, null);

        dialog.setContentView(dialogView);

        // 动画
        Window window = dialog.getWindow();
        // 设置动画
        window.setWindowAnimations(R.style.main_menu_animstyle);

        // 固定在底部
        window.setGravity(Gravity.BOTTOM);

        // 宽度全屏
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // 处理点击事件 取消
        Button cancelBt = dialogView.findViewById(R.id.user_cancel);
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // 关闭
            }
        });

        // 处理点击事件 选择相册
        Button imageDepotBt = dialogView.findViewById(R.id.image_depot);
        imageDepotBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐士意图 激活系统的东西
                Intent albumIntent = new Intent(Intent.ACTION_PICK);
                // 匹配人家系统的类型
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                // 选择相册，用户选择的结果告诉我们
                startActivityForResult(albumIntent, ALBUM_OK);

                dialog.dismiss(); // 关闭
            }
        });

        // 显示
        dialog.show();
    }

    // 选择相册后 结果 图片 拿到手

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ALBUM_OK) {
                // 相册选择成功之后， 数据在data里面
                Uri uri = data.getData();

                // 一定要裁剪  并且需要裁剪成为  1:1  调用系统的裁剪方法
                clipImage(uri);
            }

            if (requestCode == CUT_OK ) {
                // 拿到真正处理后的图片
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bitmap = extras.getParcelable("data");
                    mUserLogoIV.setImageBitmap(bitmap);

                    // TODO 上传图片到服务器
                }
            }
        }
    }

    private void clipImage(Uri uri) {
        // 选择之后的图片，进行处理
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 数据 uri 代表裁剪哪一张
        intent.setDataAndType(uri, "image/*");
        // 传递数据
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        // 你待会裁剪完之后需要获取数据   startActivityForResult
        startActivityForResult(intent, CUT_OK);
    }
}
