package com.wangxingxing.beautyfacedemo;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

import com.wangxingxing.beautyfacedemo.bean.Face;
import com.wangxingxing.beautyfacedemo.util.CameraHelper2;

public class OpenCVJNI {

    public static final int CHECK_FACE = 0x001;

    private Handler handler;
    private HandlerThread handlerThread;
    private long trackHandler;

    static {
        System.loadLibrary("native-lib");
    }

    private Face face;

    public OpenCVJNI(String path, String seeta, final CameraHelper2 cameraHelper2) {
        trackHandler = init(path, seeta);
        handlerThread = new HandlerThread("mHandler Thread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                face = native_detector(trackHandler, (byte[])msg.obj, CameraHelper2.WIDTH, CameraHelper2.HEIGHT, cameraHelper2.getCameraId());
            }
        };
    }

    public native long init(String path, String seeta);

    public native long native_startTrack(long handler);

    public native Face native_detector(long handler, byte[] data, int width, int height, int cameraID);
}
