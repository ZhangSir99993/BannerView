package com.example.bannerview.bannerview;

import java.util.ArrayList;

/**
 * Created by nana on 2018/1/28.
 */

public class DatasDealUtil {
    /**
     * 对数据列表3个以下的,进行处理,
     *
     * @param beans
     * @return 乘数因子
     */
    public static int deal(ArrayList<AdvertisementBean> beans) {
        int factor = 1;
        switch (beans.size()) {
            case 3:
                factor = 2;
                break;
            case 2:
                factor = 3;
                break;
            case 1:
                factor = 6;
                break;
        }

        return factor;
    }
}
