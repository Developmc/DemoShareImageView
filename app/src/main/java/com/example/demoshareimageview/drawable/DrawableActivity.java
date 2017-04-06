package com.example.demoshareimageview.drawable;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.demoshareimageview.R;

public class DrawableActivity extends AppCompatActivity {

    private int mSourceDrawableWidth;
    private int mSourceDrawableHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);
        //设置显示的图片
        final ImageView imageView = (ImageView)findViewById(R.id.iv_icon);
        findViewById(R.id.iv_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawableActivity.this,DrawableDetailActivity.class);
                //创建一个Rect,保存当前imageView的位置信息
                Rect rect = new Rect();
                //将位置信息赋给rect
                imageView.getGlobalVisibleRect(rect);
                //将位置信息赋给intent
                intent.setSourceBounds(rect);
                //计算imageView中显示图片的实际绘制的宽高
                calDrawableWidthAndHeight(imageView);
                intent.putExtra("sourceDrawableWidth", mSourceDrawableWidth);
                intent.putExtra("sourceDrawableHeight", mSourceDrawableHeight);
                //activity跳转
                startActivity(intent);
                //屏蔽activity跳转的默认转场效果
                overridePendingTransition(0,0);
            }
        });
    }

    /**计算imageView中显示图片的实际绘制的宽高
     * @param imageView
     */
    private void calDrawableWidthAndHeight(ImageView imageView){
        Drawable imgDrawable = imageView.getDrawable();
        if (imgDrawable != null) {
            //获得ImageView中Image的真实宽高，
            int dw = imageView.getDrawable().getBounds().width();
            int dh = imageView.getDrawable().getBounds().height();

            //获得ImageView中Image的变换矩阵
            Matrix m = imageView.getImageMatrix();
            float[] values = new float[10];
            m.getValues(values);

            //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数
            float sx = values[0];
            float sy = values[4];

            //计算Image在屏幕上实际绘制的宽高
            mSourceDrawableWidth = (int) (dw * sx);
            mSourceDrawableHeight = (int) (dh * sy);
        }
    }
}
