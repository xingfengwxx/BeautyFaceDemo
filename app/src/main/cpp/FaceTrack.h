//
// Created by WangXingxing on 2020/4/27.
//

#ifndef BEAUTYFACEDEMO_FACETRACK_H
#define BEAUTYFACEDEMO_FACETRACK_H

#include <jni.h>
#include <string>
#include<android/log.h>
#include <android/native_window_jni.h>
#include "opencv2/opencv.hpp"
#include "vector"
#include "face_alignment.h"

using namespace cv;
using namespace std;

#ifndef LOG_TAG
#define LOG_TAG "wxx"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,LOG_TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,LOG_TAG ,__VA_ARGS__) // 定义LOGF类型
#endif

class CascadeDetectorAdapter : public DetectionBasedTracker::IDetector {
public:
    CascadeDetectorAdapter(cv::Ptr<CascadeClassifier> detector) :
            IDetector(),
            Detector(detector) {
    }

    void detect(const cv::Mat &Image, std::vector<Rect> &objects) {
        Detector->detectMultiScale(Image, objects, scaleFactor, minNeighbours, 0, minObjSize,
                                   maxObjSize);
    }

    virtual ~CascadeDetectorAdapter();

private:
    CascadeDetectorAdapter();

    cv::Ptr<cv::CascadeClassifier> Detector;
};

class FaceTrack {
public:
    FaceTrack(const char *path, const char *seeta);

    void startTrack();

    vector<Rect2f> detector(const Mat src);

private:
    Ptr<DetectionBasedTracker> tracker;

    Ptr<seeta::FaceAlignment> faceAlignment;
};




#endif //BEAUTYFACEDEMO_FACETRACK_H
