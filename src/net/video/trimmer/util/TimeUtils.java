package net.video.trimmer.util;


public class TimeUtils {
	public class MilliSeconds {
		public static final int ONE_SECOND = 1000;
		public static final int ONE_MINUTE = (1000 * 60);
		public static final int ONE_HOUR = (1000 * 60 * 60);
	}
	
	public static String toFormattedTime(int time) {
		int remainingTime = time;

		int hours = remainingTime / MilliSeconds.ONE_HOUR;
		remainingTime -= hours * MilliSeconds.ONE_HOUR;

		int minutes = remainingTime / MilliSeconds.ONE_MINUTE;
		remainingTime -= minutes * MilliSeconds.ONE_MINUTE;

		int seconds = remainingTime / MilliSeconds.ONE_SECOND;

		return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) 
				: String.format("%02d:%02d", minutes, seconds);
	}

}
