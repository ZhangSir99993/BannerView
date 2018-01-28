package com.example.bannerview.bannerview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nana on 2018/1/28.
 */

public class MyBannerView extends FrameLayout {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private MyPagerAdapter mAdapter;
    private View myBanerView;
    private Context context;
    /**
     * 滑动控制器
     */
    private ViewPagerScroller pagerScroller;
    /**
     * 数据列表
     */
    private ArrayList<AdvertisementBean> datas;
    /**
     * 设置一个足够的总页数
     */
    public static int TOTAL_PAGES = 1000;
    /**
     * 是否启用轮播标志位
     */
    private boolean mIsAutoPlay = true;
    private int mCurrentItem = 0;
    private Handler mHandler = new Handler();
    /**
     * 轮播切换时间间隔
     */
    private long mDelayedTime = 1000;
    /**
     * 切换时的速度,滑动时间间隔，时间越长，自动轮播时速度越慢
     */
    private int loopSpeed;
    private String TAG = "MyBannerView";
    private final Runnable mLoopRunnable = new Runnable() {
        @Override
        public void run() {
            if (mIsAutoPlay) {
                mCurrentItem = viewPager.getCurrentItem();
                mCurrentItem++;
                if (mCurrentItem == mAdapter.getCount() - 1) {
                    mCurrentItem = 0;
                    viewPager.setCurrentItem(mCurrentItem, false);
                    mHandler.postDelayed(this, mDelayedTime);
                } else {
                    viewPager.setCurrentItem(mCurrentItem);
                    mHandler.postDelayed(this, mDelayedTime);
                }
            } else {
                mHandler.postDelayed(this, mDelayedTime);
            }
        }
    };


    public MyBannerView(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public MyBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MyBannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyBannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        myBanerView = LayoutInflater.from(context).inflate(R.layout.layout_mybaner_view, this, true);
        ButterKnife.bind(this);
        pagerScroller = new ViewPagerScroller(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyBannerView, defStyleAttr, 0);
        if (a != null) {
            loopSpeed = a.getInt(R.styleable.MyBannerView_loopSpeed, 0);
            mDelayedTime = a.getInt(R.styleable.MyBannerView_loopTime, 1000);

            a.recycle();
        }

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_DOWN:
                int paddingLeft = viewPager.getLeft();
                float touchX = ev.getRawX();
                //边沿过滤
                if (touchX >= paddingLeft && touchX < getScreenWidth(context) - paddingLeft) {
                    mIsAutoPlay = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsAutoPlay = true;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 获取要显示的第一页
     *
     * @return
     */
    private int getStartSelectItem() {
        int currentItem = TOTAL_PAGES * getRealCount() / 2;
        if (currentItem % getRealCount() == 0) {
            return currentItem;
        }
        // 直到找到从0开始的位置
        while (currentItem % getRealCount() != 0) {
            currentItem++;
        }
        return currentItem;
    }

    private int getRealCount() {
        return DatasDealUtil.deal(datas);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {

        if (viewPager != null) {
            mAdapter = new MyPagerAdapter(context, datas);
            viewPager.setAdapter(mAdapter);
            //设置预加载页数,解决滑动时,下一界面加载慢出现的闪动问题
            viewPager.setOffscreenPageLimit(2);
//尝试用方案一,进行手动矫正,效果不佳
//                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                    @Override
//                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                    }
//
//                    @Override
//                    public void onPageSelected(int position) {
//                        if (position == 0) {
//                            viewPager.setCurrentItem(3, false);
//                        }
//                        if (position == 4) {
//                            viewPager.setCurrentItem(1, false);
//                        }
//                    }
//
//                    @Override
//                    public void onPageScrollStateChanged(int state) {
//
//                    }
//                });

            viewPager.setPageTransformer(true, new CustomTransformer());
            pagerScroller.setScrollDuration(loopSpeed);
            pagerScroller.initViewPagerScroll(viewPager);
            viewPager.setCurrentItem(getStartSelectItem());
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        return width;
    }

    /**
     * 接受数据,初始化Viewpager
     *
     * @param datas
     */
    public void setDatas(ArrayList<AdvertisementBean> datas) {
        this.datas = datas;
        initViewPager();
    }

    /**
     * 启动轮播
     */
    public void startLoop() {
        if (mAdapter == null) {
            return;
        }
        mIsAutoPlay = true;
        mHandler.postDelayed(mLoopRunnable, mDelayedTime);

    }

    /**
     * 停止轮播
     */
    public void stopLoop() {
        mIsAutoPlay = false;
        mHandler.removeCallbacks(mLoopRunnable);
    }


}
