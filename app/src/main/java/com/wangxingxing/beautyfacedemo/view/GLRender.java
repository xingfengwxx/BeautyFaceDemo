package com.wangxingxing.beautyfacedemo.view;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;

import com.wangxingxing.beautyfacedemo.util.CameraHelper2;
import com.wangxingxing.beautyfacedemo.util.Utils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener, Camera.PreviewCallback {

    //思考成员变量
    public OpenGLView openGLView;
    private SurfaceTexture mSurfaceTexture;
    private int[] mTextures;
    private float[] mtx = new float[16];
    private CameraHelper2 cameraHelper2;
    private int width;
    private int height;


    public GLRender(OpenGLView openGLView) {
        this.openGLView = openGLView;
        init();
    }

    private void init() {
        // 资源拷贝
        new Thread() {
            @Override
            public void run() {
                Utils.copyAssets(openGLView.getContext(), "lbpcascade_frontalface.xml");
                Utils.copyAssets(openGLView.getContext(), "seeta_fa_v1.1.bin");
                Utils.copyAssets(openGLView.getContext(), "seeta_fd_frontal_v1.0.bin");
            }
        }.start();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        // openGL
        GLES30.glClearColor(0, 0, 0, 0);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        //根据不同的滤镜或者是filter来实现不同的效果
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(mtx);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        //摄像头的数据 定位人脸 写native的函数
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //关联camera
        cameraHelper2 = new CameraHelper2(Camera.CameraInfo.CAMERA_FACING_BACK);
        cameraHelper2.setPreviewCallback(this);
        mTextures = new int[1];
        GLES30.glGenTextures(mTextures.length, mTextures, 0);
        mSurfaceTexture = new SurfaceTexture(mTextures[0]);
        mSurfaceTexture.setOnFrameAvailableListener(this);
        mSurfaceTexture.getTransformMatrix(mtx);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        cameraHelper2.setPreviewCallback(this);
        this.width = width;
        this.height = height;

        //设计JNI的代码 主要是完成跟踪
    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }


    public void enableBeauty(boolean isChecked) {

    }

    public void enableBigEves(boolean isChecked) {

    }

    public void enableSticker(boolean isChecked) {

    }
}
