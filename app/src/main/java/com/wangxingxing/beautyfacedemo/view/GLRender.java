package com.wangxingxing.beautyfacedemo.view;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.os.Environment;

import com.blankj.utilcode.util.AppUtils;
import com.wangxingxing.beautyfacedemo.OpenCVJni;
import com.wangxingxing.beautyfacedemo.filter.BeautyFilter;
import com.wangxingxing.beautyfacedemo.filter.BigEyesFilter;
import com.wangxingxing.beautyfacedemo.filter.CameraFilter;
import com.wangxingxing.beautyfacedemo.filter.ScreenFilter;
import com.wangxingxing.beautyfacedemo.filter.StickFilter;
import com.wangxingxing.beautyfacedemo.util.CameraHelper2;
import com.wangxingxing.beautyfacedemo.util.Utils;

import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener, Camera.PreviewCallback {

    //思考成员变量
    public OpenGLView mView;
    private SurfaceTexture mSurfaceTexture;
    private int[] mTextures;
    private float[] mtx = new float[16];
    private CameraHelper2 mCameraHelper;
    private int mWidth;
    private int mHeight;
    private OpenCVJni openCVJni;

    private CameraFilter mCameraFilter;
    private BigEyesFilter mBigEyesFilter;
    private StickFilter mStickFilter;
    private BeautyFilter mBeautyFilter;
    private ScreenFilter mScreenFilter;

    private File lbpcascade_frontalface = new File(Environment.getExternalStorageDirectory() + File.separator + AppUtils.getAppPackageName(), "lbpcascade_frontalface.xml");
    private File seeta_fa = new File(Environment.getExternalStorageDirectory() + File.separator + AppUtils.getAppPackageName(), "seeta_fa_v1.1.bin");


    public GLRender(OpenGLView mView) {
        this.mView = mView;
        init();
    }

    private void init() {
        // 资源拷贝
        new Thread() {
            @Override
            public void run() {
                Utils.copyAssets(mView.getContext(), "lbpcascade_frontalface.xml");
                Utils.copyAssets(mView.getContext(), "seeta_fa_v1.1.bin");
                Utils.copyAssets(mView.getContext(), "seeta_fd_frontal_v1.0.bin");
            }
        }.start();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        //请求重新渲染
        ((OpenGLView) mView).requestRender();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        //每一帧数据会回调到data数组里
        if (openCVJni != null) {
            openCVJni.detector(data);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //关联camera
        mCameraHelper = new CameraHelper2(Camera.CameraInfo.CAMERA_FACING_BACK);
        mCameraHelper.setPreviewCallback(this);
        mTextures = new int[1];
        //创建纹理id
        GLES20.glGenTextures(mTextures.length, mTextures, 0);
        //SurfaceTexture与纹理id相关联，方便摄像头的数据能够传递给OpenGL
        mSurfaceTexture = new SurfaceTexture(mTextures[0]);
        //设置监听
        mSurfaceTexture.setOnFrameAvailableListener(this);
        //获取摄像头的矩阵，不会变形
        mSurfaceTexture.getTransformMatrix(mtx);

        mCameraFilter = new CameraFilter(mView.getContext());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mCameraHelper.setPreviewCallback(this);
        this.mWidth = width;
        this.mHeight = height;
        mCameraFilter.onReady(width, height);
        //设计JNI的代码 主要是完成跟踪
        //        mBigEyesFilter.onReady(width, height);
//        mStickFilter.onReady(width, height);
        mScreenFilter.onReady(width, height);
        if (openCVJni == null) {
            openCVJni = new OpenCVJni(lbpcascade_frontalface.getAbsolutePath(), seeta_fa.getAbsolutePath(), mCameraHelper);
        }
        openCVJni.startTrack();
    }

    //摄像头获取一帧数据会回调该方法
    @Override
    public void onDrawFrame(GL10 gl) {
        //清空成黑色
        GLES20.glClearColor(0, 0, 0, 0);
        //把之前的都清空
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(mtx);
        mCameraFilter.setMatrix(mtx);
        int id = mCameraFilter.onDrawFrame(mTextures[0]);
        if (mBigEyesFilter != null) {
            mBigEyesFilter.setFace(openCVJni.getFace());
            id = mBigEyesFilter.onDrawFrame(id); //大眼效果
        }
        if (mStickFilter != null) {
            mStickFilter.setFace(openCVJni.getFace());
            id = mScreenFilter.onDrawFrame(id); //贴纸效果
        }
        if (mBeautyFilter != null) {
            id = mBeautyFilter.onDrawFrame(id); //美颜效果
        }
        //mScreenFilter将最终的特效运用到 SurfaceView中
        mScreenFilter.onDrawFrame(id);
    }


    public void enableBeauty(final boolean isChecked) {
        //因为操作滤镜只能在 GLThread里面操作，所以要这样写
        mView.queueEvent(new Runnable() {
            @Override
            public void run() {
                if (isChecked) {
                    mBeautyFilter = new BeautyFilter(mView.getContext());
                    mBeautyFilter.onReady(mWidth, mHeight);
                } else {
                    mBeautyFilter = null;
                }
            }
        });
    }

    public void enableBigEves(final boolean isChecked) {
        //因为操作滤镜只能在 GLThread里面操作，所以要这样写
        mView.queueEvent(new Runnable() {
            @Override
            public void run() {
                if (isChecked) {
                    mBigEyesFilter = new BigEyesFilter(mView.getContext());
                    mBigEyesFilter.onReady(mWidth, mHeight);
                } else {
                    mBigEyesFilter = null;
                }
            }
        });
    }

    public void enableSticker(final boolean isChecked) {
        //因为操作滤镜只能在 GLThread里面操作，所以要这样写
        mView.queueEvent(new Runnable() {
            @Override
            public void run() {
                if (isChecked) {
                    mStickFilter = new StickFilter(mView.getContext());
                    mStickFilter.onReady(mWidth, mHeight);
                } else {
                    mStickFilter = null;
                }
            }
        });
    }
}
