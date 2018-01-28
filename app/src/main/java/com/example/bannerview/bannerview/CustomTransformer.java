package com.example.bannerview.bannerview;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by nana on 2018/1/28.
 */

public class CustomTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.9F;

    @Override
    public void transformPage(View page, float position) {
/**
 *滑动时缩放效果
 */
        if (position < -1) {
            page.setScaleY(MIN_SCALE);
        } else if (position <= 1) {
            float scale = Math.max(MIN_SCALE, 1 - Math.abs(position));
            page.setScaleY(scale);

        } else {
            page.setScaleY(MIN_SCALE);
        }
    }

}
