package com.wangxingxing.beautyfacedemo.bean;


import java.util.Arrays;

public class Face {
    // 人眼的数据
    public float[] eyesRect;
    public int width;
    public int height;
    public int imgWidth;
    public int imgHeight;

    public Face(float[] eyesRect, int width, int height, int imgWidth, int imgHeight) {
        this.eyesRect = eyesRect;
        this.width = width;
        this.height = height;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
    }

    @Override
    public String toString() {
        return "Face{" +
                "eyesRect=" + Arrays.toString(eyesRect) +
                ", width=" + width +
                ", height=" + height +
                ", imgWidth=" + imgWidth +
                ", imgHeight=" + imgHeight +
                '}';
    }
}
