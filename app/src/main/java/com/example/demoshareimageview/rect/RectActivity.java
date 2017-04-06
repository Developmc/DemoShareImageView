package com.example.demoshareimageview.rect;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.demoshareimageview.R;

public class RectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rect);
        //设置显示的图片
        final ImageView imageView = (ImageView)findViewById(R.id.iv_icon);
        findViewById(R.id.iv_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RectActivity.this,RectDetailActivity.class);
                //创建一个Rect,保存当前imageView的位置信息
                Rect rect = new Rect();
                //将位置信息赋给rect
                imageView.getGlobalVisibleRect(rect);
                //将位置信息赋给intent
                intent.setSourceBounds(rect);
                //activity跳转
                startActivity(intent);
                //屏蔽activity跳转的默认转场效果
                overridePendingTransition(0,0);
            }
        });
    }
}
