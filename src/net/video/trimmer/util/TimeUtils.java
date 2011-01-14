package net.video.trimmer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

	public static String toFormattedTime(int time) {
		Date date = new Date(time);
		return new SimpleDateFormat(date.getHours() > 0 ? "HH:mm:ss" : "mm:ss").format(date);
	}

}
