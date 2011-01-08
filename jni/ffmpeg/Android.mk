MY_PATH:=$(call my-dir)

LOCAL_PATH:= $(MY_PATH)/libavutil
include $(CLEAR_VARS)
LOCAL_MODULE := libavutil
include $(LOCAL_PATH)/../av.mk
LOCAL_SRC_FILES := $(FFFILES)
LOCAL_C_INCLUDES := $(LOCAL_PATH) $(LOCAL_PATH)/..
LOCAL_CFLAGS += $(FFCFLAGS)
include $(BUILD_STATIC_LIBRARY)

LOCAL_PATH:= $(MY_PATH)/libavcodec
include $(CLEAR_VARS)
LOCAL_MODULE := libavcodec
include $(LOCAL_PATH)/../av.mk
LOCAL_SRC_FILES := $(FFFILES)
LOCAL_C_INCLUDES := $(LOCAL_PATH) $(LOCAL_PATH)/..
LOCAL_CFLAGS += $(FFCFLAGS)
LOCAL_LDLIBS += -lz
LOCAL_STATIC_LIBRARIES := libavutil
include $(BUILD_STATIC_LIBRARY)

LOCAL_PATH:= $(MY_PATH)/libavformat
include $(CLEAR_VARS)
LOCAL_MODULE := libavformat
include $(LOCAL_PATH)/../av.mk
LOCAL_SRC_FILES := $(FFFILES)
LOCAL_C_INCLUDES := $(LOCAL_PATH) $(LOCAL_PATH)/..
LOCAL_CFLAGS += $(FFCFLAGS)
LOCAL_LDLIBS += -lz
LOCAL_STATIC_LIBRARIES := libavutil libavcodec
include $(BUILD_STATIC_LIBRARY)

LOCAL_PATH:= $(MY_PATH)/libswscale
include $(CLEAR_VARS)
LOCAL_MODULE := libswscale
include $(LOCAL_PATH)/../av.mk
LOCAL_SRC_FILES := $(FFFILES)
LOCAL_C_INCLUDES := $(LOCAL_PATH) $(LOCAL_PATH)/..
LOCAL_CFLAGS += $(FFCFLAGS)
LOCAL_LDLIBS += -lz
LOCAL_STATIC_LIBRARIES := libavutil
include $(BUILD_STATIC_LIBRARY)


LOCAL_PATH := $(MY_PATH)
include $(CLEAR_VARS)
LOCAL_MODULE    := ffmpeg
LOCAL_SRC_FILES := ffmpeg.c	\
					cmdutils.c
				
LOCAL_CFLAGS += \
	-include "ctype.h"   \
	-include "string.h"   \
	-include "math.h"   \
	-include "stdlib.h"   \
	-include "errno.h"   \
	-include "signal.h"   \
	-include "limits.h"   \
	-include "unistd.h"   \
	-include "sys/types.h"   \
	-include "sys/time.h"   \
	-include "sys/resource.h"   \
	-include "sys/select.h"   \
	-include "fcntl.h"   \
	-include "sys/ioctl.h"   \
	-include "sys/time.h"   \
	-include "termios.h"   \
	-include "time.h"   \
	-include "assert.h"   
				
LOCAL_STATIC_LIBRARIES := libavformat libswscale libavcodec libavutil 
LOCAL_LDLIBS += -lz -llog
include $(BUILD_SHARED_LIBRARY)
