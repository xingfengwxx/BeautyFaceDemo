package com.wangxingxing.beautyfacedemo.filter;

import android.content.Context;

import com.wangxingxing.beautyfacedemo.R;

public class BeautyFilter extends BaseFilter {

    public BeautyFilter(Context context) {
        super(context, R.raw.beauty, R.raw.beauty);
    }

    @Override
    protected void initCoordinate(Context context) {
        //真正的自己的坐标
        float[] texture = {
                0.0f,1.0f,
                1.0f,1.0f,
                0.0f,0.0f,
                1.0f,0.0f,
        };
    }

    @Override
    public void onReady(int width, int height) {
        super.onReady(width, height);
    }

    @Override
    public void onDrawFrame(int textureID) {
        super.onDrawFrame(textureID);
        //覆盖？
    }
}
