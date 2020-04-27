package com.wangxingxing.beautyfacedemo.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class OpenGLView extends GLSurfaceView {

    public GLRender glRender;

    public OpenGLView(Context context) {
        super(context);
    }

    public OpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(3);
        glRender = new GLRender(this);
        setRenderer(glRender);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    //封装一些函数

    public void enableBeauty(boolean isChecked) {
        glRender.enableBeauty(isChecked);
    }

    public void enableBigEves(boolean isChecked) {
        glRender.enableBigEves(isChecked);
    }

    public void enableSticker(boolean isChecked) {
        glRender.enableSticker(isChecked);
    }
}
