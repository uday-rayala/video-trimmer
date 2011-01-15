package net.video.trimmer.util;

import junit.framework.Assert;

import org.junit.Test;

public class TimeUtilsTest {
	@Test
	public void shouldReturnFormattedTime() {
		Assert.assertEquals("00:03", TimeUtils.toFormattedTime(seconds(3)));
		Assert.assertEquals("00:50", TimeUtils.toFormattedTime(seconds(50)));
		Assert.assertEquals("02:50", TimeUtils.toFormattedTime(minutes(2) + seconds(50)));
		Assert.assertEquals("32:55", TimeUtils.toFormattedTime(minutes(32) + seconds(55)));
		Assert.assertEquals("03:32:55", TimeUtils.toFormattedTime(hours(3) + minutes(32) + seconds(55)));
	}

	private int seconds(int num) {
		return num * 1000;
	}

	private int minutes(int num) {
		return num * 60 * 1000;
	}

	private int hours(int num) {
		return num * 60 * 60 * 1000;
	}
}
