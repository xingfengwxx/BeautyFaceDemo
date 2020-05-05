package com.wangxingxing.beautyfacedemo;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

import com.wangxingxing.beautyfacedemo.bean.Face;
import com.wangxingxing.beautyfacedemo.util.CameraHelper2;

public class OpenCVJni {

    static {
        System.loadLibrary("native-lib");
    }

    public static final int CHECK_FACE = 1;

    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private long trackHandler;
    private Face face;

    public OpenCVJni(String path, String seeta, final CameraHelper2 cameraHelper2) {
        trackHandler = init(path, seeta);
        mHandlerThread = new HandlerThread("mHandler Thread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                face = native_detector(trackHandler, (byte[])msg.obj, CameraHelper2.WIDTH, CameraHelper2.HEIGHT, cameraHelper2.getCameraId());
            }
        };
    }

    public void startTrack() {
        native_startTrack(trackHandler);
    }

    public Face getFace() {
        return face;
    }

    //先检测人脸，再检测人眼  由于比较耗时，要开启线程进行检测
    public void detector(byte[] data) {
        //先把之前的消息移除
        mHandler.removeMessages(CHECK_FACE);
        Message message = mHandler.obtainMessage(CHECK_FACE);
        message.obj = data;
        mHandler.sendMessage(message);
    }

    //加载训练库
    public native long init(String path, String seeta);

    //开启检测
    public native long native_startTrack(long trackHandler);

    //检测人脸、人眼
    public native Face native_detector(long trackHandler, byte[] data, int width, int height, int cameraID);
}
