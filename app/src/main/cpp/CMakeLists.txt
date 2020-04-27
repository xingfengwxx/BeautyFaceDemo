cmake_minimum_required(VERSION 3.4.1)

#set(CMAKE_C_STANDARD 98)
set(CMAKE_CXX_STANDARD 14)
include_directories("include")

file(GLOB my_source_path ${CMAKE_SOURCE_DIR}/*.cpp ${CMAKE_SOURCE_DIR}/*.c)
#MESSAGE(${my_source_path})
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../../../libs/${CMAKE_ANDROID_ARCH_ABI}")

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ${my_source_path})


find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

add_subdirectory(FaceAlignment)
include_directories(FaceAlignment/include)

target_link_libraries( # Specifies the target library.
        native-lib
        opencv_java4

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        android
        seeta_fa_lib)