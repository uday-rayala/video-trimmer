#include <jni.h>
#include "include/net_video_trimmer_natives_VideoTrimmer.h"
#include "libswscale/swscale.h"
#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "ffmpeg.h"
#include <stdlib.h>
#include <string.h>
#include <android/log.h>

void log_message(char* message) {
	__android_log_write(ANDROID_LOG_ERROR, "VideoTrimmer", message);
}

void reverse(char s[])
 {
     int i, j;
     char c;

     for (i = 0, j = strlen(s)-1; i<j; i++, j--) {
         c = s[i];
         s[i] = s[j];
         s[j] = c;
     }
 }

/* itoa:  convert n to characters in s */
void itoa(int n, char s[])
{
    int i, sign;

    if ((sign = n) < 0)  /* record sign */
        n = -n;          /* make n positive */
    i = 0;
    do {       /* generate digits in reverse order */
        s[i++] = n % 10 + '0';   /* get next digit */
    } while ((n /= 10) > 0);     /* delete it */
    if (sign < 0)
        s[i++] = '-';
    s[i] = '\0';
    reverse(s);
}



JNIEXPORT jint JNICALL Java_net_video_trimmer_natives_VideoTrimmer_trim(JNIEnv *env,
		jclass someclass, jstring inputFile, jstring outFile, jint startTime,
		jint length) {
	log_message("Starting to cut");

	int numberOfArgs = 12;
	char** arguments = calloc(numberOfArgs, sizeof(char*));
	char start[5], duration[5];
	const char *in, *out;
	itoa(startTime, start);
	itoa(length, duration);

	in = (*env)->GetStringUTFChars(env, inputFile, 0);
	out = (*env)->GetStringUTFChars(env, outFile, 0);

	arguments[0] = "ffmpeg";
	arguments[1] = "-i";
	arguments[2] = in;
	arguments[3] = "-ss";
	arguments[4] = start;
	arguments[5] = "-t";
	arguments[6] = duration;
	arguments[7] = "-vcodec";
	arguments[8] = "copy";
	arguments[9] = "-acodec";
	arguments[10] = "copy";
//	arguments[11] = "-strict";
//	arguments[12] = "experimental";
//	arguments[13] = "-ab";
//	arguments[14] = "12k";
	arguments[11] = out;

	int i;
	for (i = 0; i < numberOfArgs; i++) {
		log_message(arguments[i]);
	}
	log_message("Printed all");

	ffmpeg_main(numberOfArgs, arguments);
	log_message("Finished cutting");
	free(arguments);
	(*env)->ReleaseStringUTFChars(env, inputFile, in);
	(*env)->ReleaseStringUTFChars(env, outFile, out);
	return 0;
}

