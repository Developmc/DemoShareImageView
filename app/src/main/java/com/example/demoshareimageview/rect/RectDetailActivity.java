package com.example.demoshareimageview.rect;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.demoshareimageview.R;

public class RectDetailActivity extends AppCompatActivity {

    private ImageView mImageView;
    //动画时间
    public static final int DURATION = 300;
    //动画插值器
    private static final AccelerateDecelerateInterpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    //上一个界面的图片位置信息
    private Rect mSourceRect;
    //上一个界面的图片的宽度
    private int mSourceWidth;
    //上一个界面的图片的高度
    private int mSourceHeight;
    //当前界面的目标图片的位置
    private Rect mTargetRect = new Rect();
    //前后两个图片的收缩比
    private float mScaleWidth, mScaleHeight;
    //前后两个图片的位移距离
    private float mTransitionX, mTransitionY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rect_detail);
        //初始化控件
        initView();
        //初始化场景
        initBehavior();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        mImageView = (ImageView)findViewById(R.id.iv_icon);
        //设置图片,也可以直接在xml布局中设置：android:src="@drawable/ic_one_piece"
        mImageView.setImageResource(R.drawable.ic_one_piece);
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
        //当界面的imageView测量完成后，即高度和宽度确定后
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                //获取目标imageView在布局中的位置
                mImageView.getGlobalVisibleRect(mTargetRect);
                //更改mImageView的位置，使其和上一个界面的图片的位置重合
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mSourceWidth,mSourceHeight);
                params.setMargins(mSourceRect.left,mSourceRect.top-getStatusBarHeight()-getActionBarHeight(),
                        mSourceRect.right,mSourceRect.bottom);
                mImageView.setLayoutParams(params);
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
        // 计算目标imageView的宽高
        int targetWidth = mTargetRect.right - mTargetRect.left;
        int targetHeight = mTargetRect.bottom - mTargetRect.top;

        //获得收缩比
        mScaleWidth = (float) targetWidth / mSourceWidth;
        mScaleHeight = (float) targetHeight / mSourceHeight;
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
        mImageView.animate()
                .setInterpolator(DEFAULT_INTERPOLATOR)
                .setDuration(DURATION)
                .scaleX(mScaleWidth)
                .scaleY(mScaleHeight)
                .translationX(mTransitionX)
                .translationY(mTransitionY)
                //withEndAction要求版本在16以上
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                    }
                })
                .start();
    }

    /**
     * 退场动画:属性动画
     */
    private void runExitAnim() {
        mImageView.animate()
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
