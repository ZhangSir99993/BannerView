package com.example.bannerview.bannerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.banner_view)
    MyBannerView bannerView;
    private ArrayList<AdvertisementBean> beans = new ArrayList<AdvertisementBean>();
    AdvertisementBean advertisementBean1 = new AdvertisementBean();
    AdvertisementBean advertisementBean2 = new AdvertisementBean();
    AdvertisementBean advertisementBean3 = new AdvertisementBean();
    AdvertisementBean advertisementBean4 = new AdvertisementBean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //模拟网络请求数据
        advertisementBean1.setResourId(R.layout.view_one);
        advertisementBean1.setToastString("页面1");
        advertisementBean2.setResourId(R.layout.view_two);
        advertisementBean2.setToastString("页面2");
        advertisementBean3.setResourId(R.layout.view_three);
        advertisementBean3.setToastString("页面3");
        advertisementBean4.setResourId(R.layout.view_four);
        advertisementBean4.setToastString("页面4");
        beans.add(advertisementBean1);
        beans.add(advertisementBean2);
        beans.add(advertisementBean3);
        beans.add(advertisementBean4);

        bannerView.setDatas(beans);

    }


    @Override
    protected void onResume() {
        super.onResume();
        //运行状态时启动轮播效果
        bannerView.startLoop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //及时停掉自动播放,不然会导致播放错乱
        bannerView.stopLoop();

    }
}
