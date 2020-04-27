package com.wangxingxing.beautyfacedemo.filter;

import android.content.Context;
import android.opengl.GLES30;

import com.wangxingxing.beautyfacedemo.util.OpenGLUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 着色器的封装
 */
public abstract class BaseFilter {
    public int vertexShaderID;
    public int fragmentShaderID;
    public FloatBuffer floatBuffer;
    public FloatBuffer mTextureBuffer;

    public int vPosition = -1;
    public int vCoord = -1;
    public int vMartix = -1;
    public int vTexture = -1;
    public int mProgram;

    private int width;
    private int height;

    public BaseFilter(Context context, int vertexShaderID, int fragmentShaderID) {
        this.vertexShaderID = vertexShaderID;
        this.fragmentShaderID = fragmentShaderID;

        floatBuffer = ByteBuffer.allocate(4 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.clear();

        //定义openGL的顶点坐标
        float[] vertex = {
                -1.0f,-1.0f,
                1.0f,-1.0f,
                -1.0f,1.0f,
                1.0f,1.0f,
        };

        floatBuffer.put(vertex);

        mTextureBuffer = ByteBuffer.allocate(4 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextureBuffer.clear();
        //手机坐标 camera坐标
        float[] texture = {
                0.0f,1.0f,
                1.0f,1.0f,
                0.0f,0.0f,
                1.0f,0.0f,
        };

        mTextureBuffer.put(texture);
        initial(context);
        initCoordinate(context);
    }

    protected abstract void initCoordinate(Context context);

    private void initial(Context context) {
        mProgram = OpenGLUtil.loadProgram(OpenGLUtil.readRawTextFile(context, vertexShaderID),
                OpenGLUtil.readRawTextFile(context, fragmentShaderID));

        vPosition = GLES30.glGetAttribLocation(mProgram, "vPosition");
        vCoord = GLES30.glGetAttribLocation(mProgram, "vCoord");
        vMartix = GLES30.glGetAttribLocation(mProgram, "vMartix");
        vTexture = GLES30.glGetAttribLocation(mProgram, "vTexture");
    }

    public void onReady(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void onDrawFrame(int textureID) {
        GLES30.glViewport(0, 0, width, height);
        GLES30.glUseProgram(mProgram);

        floatBuffer.position(0);
        GLES30.glEnableVertexAttribArray(vPosition);
        GLES30.glVertexAttribPointer(vPosition, 2, GLES30.GL_FLOAT, false, 0, mTextureBuffer);
        mTextureBuffer.position(0);
        GLES30.glEnableVertexAttribArray(vCoord);
        GLES30.glVertexAttribPointer(vCoord, 2, GLES30.GL_FLOAT, false, 0, mTextureBuffer);

        //激活、绑定采样器
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(vTexture, 0);

        GLES30.glUniform1i(vTexture, 0);

        GLES30.glUniform1i(vTexture, 0);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);
    }
}
