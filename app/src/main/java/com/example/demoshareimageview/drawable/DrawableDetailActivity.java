package com.example.demoshareimageview.drawable;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.demoshareimageview.R;

public class DrawableDetailActivity extends AppCompatActivity {

    private FrameLayout llRootView;
    private ImageView mImageView;
    private ImageView mTempImageView;
    //动画时间
    public static final int DURATION = 300;
    //动画插值器
    private static final AccelerateDecelerateInterpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    //上一个界面的图片位置信息
    private Rect mSourceRect;
    //上一个界面的imageView的宽度
    private int mSourceWidth;
    //上一个界面的imageView的高度
    private int mSourceHeight;
    //上一个界面，imageView内容绘制图片的实际宽度
    private int mSourceDrawableWidth;
    //上一个界面，imageView内容绘制图片的实际高度
    private int mSourceDrawableHeight;
    //当前界面的目标图片的位置
    private Rect mTargetRect = new Rect();
    //前后两个图片的收缩比
    private float mScaleWidth, mScaleHeight;
    //前后两个图片的位移距离
    private float mTransitionX, mTransitionY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_detail);
        //初始化控件
        initView();
        //初始化场景
        initBehavior();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        llRootView = (FrameLayout)findViewById(R.id.ll_rootView);
        mImageView = (ImageView)findViewById(R.id.iv_icon);
        //设置图片,也可以直接在xml布局中设置：android:src="@drawable/ic_one_piece"
        mImageView.setImageResource(R.drawable.ic_picture4);
    }

    /**
     * 初始化场景
     */
    private void initBehavior(){
        //获取上一个界面传过来的Rect
        mSourceRect = getIntent().getSourceBounds();
        //计算上一个界面图片的宽度和高度
        mSourceWidth = mSourceRect.right-mSourceRect.left;
        mSourceHeight = mSourceRect.bottom-mSourceRect.top;
        mSourceDrawableWidth = getIntent().getIntExtra("sourceDrawableWidth",0);
        mSourceDrawableHeight = getIntent().getIntExtra("sourceDrawableHeight",0);
        //当界面的imageView测量完成后，即高度和宽度确定后
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                //获取目标imageView在布局中的位置
                mImageView.getGlobalVisibleRect(mTargetRect);
                mTempImageView = new ImageView(DrawableDetailActivity.this);
                mTempImageView.setImageResource(R.drawable.ic_picture4);
                mTempImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                //更改mTempImageView的位置，使其和上一个界面的图片的位置重合
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mSourceWidth,mSourceHeight);
                params.setMargins(mSourceRect.left,mSourceRect.top-getStatusBarHeight()-getActionBarHeight(),mSourceRect.right,mSourceRect.bottom);
                mTempImageView.setLayoutParams(params);
                //把view添加进来
                llRootView.addView(mTempImageView);
                //计算图片缩放比例和位移
                calculateInfo();
                // 设置入场动画
                runEnterAnim();
            }
        });
    }

    /**
     * 获取状态栏高度
     * @return
     */
    private int getStatusBarHeight() {
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return getResources().getDimensionPixelSize(resourceId);
        }
        return -1;
    }

    /**获取ActionBar高度
     * @return
     */
    private int getActionBarHeight(){
        //如果有ActionBar
        if(getSupportActionBar()!=null){
            return getSupportActionBar().getHeight();
        }
        return 0;
    }
    /**
     * 计算图片缩放比例，以及位移距离
     *
     */
    private void calculateInfo() {
        // 计算目标imageView的中实际绘制图片的宽高
        int targetDrawableWidth = 0;
        int targetDrawableHeight = 0;
        Drawable imgDrawable = mImageView.getDrawable();
        if (imgDrawable != null) {
            //获得ImageView中Image的真实宽高，
            int dw = mImageView.getDrawable().getBounds().width();
            int dh = mImageView.getDrawable().getBounds().height();

            //获得ImageView中Image的变换矩阵
            Matrix m = mImageView.getImageMatrix();
            float[] values = new float[10];
            m.getValues(values);

            //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数
            float sx = values[0];
            float sy = values[4];

            //计算Image在屏幕上实际绘制的宽高
            targetDrawableWidth = (int) (dw * sx);
            targetDrawableHeight = (int) (dh * sy);
        }

        //获得收缩比
        mScaleWidth = (float) targetDrawableWidth / mSourceDrawableWidth;
        mScaleHeight = (float) targetDrawableHeight / mSourceDrawableHeight;
        //x,y上的位移
        mTransitionX = (mTargetRect.left+(mTargetRect.right - mTargetRect.left) / 2)
                - (mSourceRect.left + (mSourceRect.right - mSourceRect.left) / 2);
        mTransitionY = (mTargetRect.top + (mTargetRect.bottom - mTargetRect.top) / 2)
                - (mSourceRect.top + (mSourceRect.bottom - mSourceRect.top) / 2);
    }

    /**
     * 入场动画:属性动画
     */
    private void runEnterAnim() {
        mTempImageView.animate()
                .setInterpolator(DEFAULT_INTERPOLATOR)
                .setDuration(DURATION)
                .scaleX(mScaleWidth)
                .scaleY(mScaleHeight)
                .translationX(mTransitionX)
                .translationY(mTransitionY)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mTempImageView.setVisibility(View.INVISIBLE);
                        mImageView.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }

    /**
     * 退场动画:属性动画
     */
    private void runExitAnim() {
        if(mTempImageView==null){
            finish();
            return;
        }
        mImageView.setVisibility(View.INVISIBLE);
        mTempImageView.setVisibility(View.VISIBLE);
        mTempImageView.animate()
                .setInterpolator(DEFAULT_INTERPOLATOR)
                .setDuration(DURATION)
                .scaleX(1)
                .scaleY(1)
                .translationX(0)
                .translationY(0)
                //withEndAction要求版本在16以上
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();
    }

    @Override
    public void onBackPressed() {
        // 使用退场动画
        runExitAnim();
    }

}
