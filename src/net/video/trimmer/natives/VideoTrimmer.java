package net.video.trimmer.natives;

public class VideoTrimmer {
	public static native int trim(String inputFile, String outputFile, int start, int duration);
}
