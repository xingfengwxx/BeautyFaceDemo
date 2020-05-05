package com.wangxingxing.beautyfacedemo.filter;

import android.content.Context;

import com.wangxingxing.beautyfacedemo.R;


/*
    滤镜
    作为显示滤镜  显示CameraFilter已经渲染好的特效
 */
public class ScreenFilter extends AbstractFilter {

    public ScreenFilter(Context context) {
        super(context, R.raw.base_vertex, R.raw.base_frag);
    }

    @Override
    protected void initCoordinate() {

    }
}
