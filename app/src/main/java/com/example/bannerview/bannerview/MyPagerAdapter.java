package com.example.bannerview.bannerview;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by nana on 2018/1/28.
 */
public class MyPagerAdapter extends PagerAdapter {
    private ArrayList<AdvertisementBean> dataLists;
    private Context context;
    private String TAG = "MyPagerAdapter";

    public MyPagerAdapter(Context context
            , ArrayList<AdvertisementBean> dataLists) {
        super();
        this.dataLists = dataLists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return MyBannerView.TOTAL_PAGES * getRealCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final int realPosition = position % getRealCount();
        View view = getDatas().get(realPosition);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //广告位点击事件
                if (dataLists.size() < 4) {
                    Toast.makeText(context, dataLists.get(realPosition % dataLists.size()).getToastString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, dataLists.get(realPosition).getToastString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        final int realPosition = position % getRealCount();
        container.removeView(getDatas().get(realPosition));
    }

    /**
     * 获取真实的Count
     *
     * @return
     */
    private int getRealCount() {
        return getDatas() == null ? 0 : getDatas().size();
    }

    /**
     * 对列表数小于3的数据进行处理
     * @return 返回个数为6的新列表
     */
    private ArrayList<View> getDatas() {
        ArrayList<View> viewLists = new ArrayList<View>();
        LayoutInflater li = ((Activity) context).getLayoutInflater();

        for (int j = 0; j < DatasDealUtil.deal(dataLists); j++) {
            for (int i = 0; i < dataLists.size(); i++) {
                viewLists.add(li.inflate(dataLists.get(i).getResourId(), null, false));
            }
        }

        return viewLists;

    }
}