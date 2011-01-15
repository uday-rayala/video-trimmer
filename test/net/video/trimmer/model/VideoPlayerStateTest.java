package net.video.trimmer.model;

import junit.framework.Assert;

import org.junit.Test;

public class VideoPlayerStateTest {
	@Test
	public void shouldValidateTheState() {
		VideoPlayerState state = new VideoPlayerState();
		state.setStart(5);
		state.setStop(10);
		Assert.assertTrue(state.isValid());
	}
	
	@Test
	public void shouldInValidateWhenStartIsAheadOfEnd() {
		VideoPlayerState state = new VideoPlayerState();
		state.setStart(10);
		state.setStop(5);
		Assert.assertFalse(state.isValid());
	}
}
