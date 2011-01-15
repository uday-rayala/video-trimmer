package net.video.trimmer.util;


public class TimeUtils {

	public static String toFormattedTime(int time) {
		int remainingTime = time;

		int hours = remainingTime / (1000 * 60 * 60);
		remainingTime -= hours * (1000 * 60 * 60);

		int minutes = remainingTime / (1000 * 60);
		remainingTime -= minutes * (1000 * 60);

		int seconds = remainingTime / 1000;

		return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) 
				: String.format("%02d:%02d", minutes, seconds);
	}

}
