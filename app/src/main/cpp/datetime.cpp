//
// Created by silence on 2018/11/14.
//

#include "datetime.h"
#include <ctime>
#include <iostream>
#include <string>
//using std::cout;
using namespace std;

jstring  Java_com_example_silence_myclock_util_Util_hello
        (JNIEnv *env, jobject obj) {
    char *cstr = "Hello from C";
    return env->NewStringUTF(cstr);
}

jstring  Java_com_example_silence_myclock_util_Util_timeStr__
        (JNIEnv *env, jobject obj) {
    time_t seconds;
    time(&seconds);
    struct tm *localTime = localtime(&seconds);
    char fmt[] = "%A %B %d %H:%M:%S %Y";
    char strTime[64];
    if (strftime(strTime, 64, fmt, localTime)){
        return env->NewStringUTF(strTime);
    }
    return env->NewStringUTF("unknown");
}

jstring  Java_com_example_silence_myclock_util_Util_timeStr__Ljava_lang_String_2
        (JNIEnv *env, jobject obj, jstring fmt) {
    jboolean t = 1;
    jchar const *fmtJC = env->GetStringChars(fmt, &t);

    // 获取当前本地时间
    time_t seconds;
    time(&seconds);
    struct tm *localTime = localtime(&seconds);
    // 判断参数格式说明符号
//    strftime(fmtJC, )
    string str;
    return env->NewStringUTF("hello");
}