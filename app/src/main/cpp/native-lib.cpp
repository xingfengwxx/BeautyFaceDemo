#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_wangxingxing_beautyfacedemo_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_wangxingxing_beautyfacedemo_OpenCVJNI_init(JNIEnv *env, jobject thiz, jstring path,
                                                    jstring seeta) {
    // TODO: implement init()
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_wangxingxing_beautyfacedemo_OpenCVJNI_native_1startTrack(JNIEnv *env, jobject thiz,
                                                                  jlong handler) {
    // TODO: implement native_startTrack()
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_wangxingxing_beautyfacedemo_OpenCVJNI_native_1detector(JNIEnv *env, jobject thiz,
                                                                jlong handler, jbyteArray data,
                                                                jint width, jint height,
                                                                jint camera_id) {
    // TODO: implement native_detector()
}