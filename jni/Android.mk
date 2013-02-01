LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_CFLAGS := -g
LOCAL_LDLIBS := -llog
LOCAL_MODULE := board
LOCAL_SRC_FILES := board.c

include $(BUILD_SHARED_LIBRARY)