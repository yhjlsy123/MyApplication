package com.zhuochi.hydream.bathhousekeeper.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MyMultipleItem implements MultiItemEntity {
    public static final int FIRST_TYPE = 1;
    public static final int SECOND_TYPE = 2;

    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }


}
