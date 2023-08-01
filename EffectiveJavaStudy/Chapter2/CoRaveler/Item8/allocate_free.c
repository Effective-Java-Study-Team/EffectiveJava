#include <jni.h>
#include "NativeResource.h"

JNIEXPORT jlong JNICALL Java_NativeResource_allocate
  (JNIEnv *env, jclass cls) {
    // 메모리를 할당하고 포인터를 반환합니다.
    void* memory = malloc(100);
    return (jlong)memory;
}

JNIEXPORT void JNICALL Java_NativeResource_free
  (JNIEnv *env, jclass cls, jlong handle) {
    // 할당된 메모리를 해제합니다.
    free((void*)handle);
}
