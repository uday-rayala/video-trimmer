package net.video.trimmer.natives;

public class VideoTrimmer {
	public static native int trim(String inputFile, String outputFile, int start, int duration);

	public int trim_(String inputFileName, String outFileName, int start, int duration) {
		return trim(inputFileName, outFileName, start, duration);
	}
}
