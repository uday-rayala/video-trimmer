MY_LOCAL_PATH := $(call my-dir)
include $(all-subdir-makefiles)

LOCAL_PATH :=$(MY_LOCAL_PATH)
include $(CLEAR_VARS)
LOCAL_MODULE    := video-trimmer
LOCAL_SRC_FILES := video-trimmer.c
LOCAL_C_INCLUDES := $(MY_LOCAL_PATH) $(MY_LOCAL_PATH)/ffmpeg
LOCAL_SHARED_LIBRARIES := ffmpeg 
LOCAL_LDLIBS += -lz -llog
include $(BUILD_SHARED_LIBRARY)
