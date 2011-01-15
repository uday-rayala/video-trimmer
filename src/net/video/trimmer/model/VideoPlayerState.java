package net.video.trimmer.model;

public class VideoPlayerState {
	private String filename;
	private int start = 0, stop = 0;
	private int currentTime=0;
	private String messageText;

	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getStop() {
		return stop;
	}
	public void setStop(int stop) {
		this.stop = stop;
	}
	public void reset() {
		start = stop = 0;
	}
	public int getDuration() {
		// TODO Auto-generated method stub
		return stop - start;
	}
	public int getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public boolean isValid() {
		return stop > start;
	}
}
