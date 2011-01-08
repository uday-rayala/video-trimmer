package net.video.trimmer;

public class VideoTrimmer {
	public static native int trim(String inputFile, String outputFile, int start, int duration);
}
