package net.video.trimmer.service;

import net.video.trimmer.natives.VideoTrimmer;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.test.ServiceTestCase;

public class VideoTrimmingServiceTest extends ServiceTestCase<VideoTrimmingService> {

	private Context context;
	private VideoTrimmingService service;
	private static final String inputFileName = "input.3gp";
	private static final String outputFileName = "output.3gp";
	private static final int startTime = 10;
	private static final int duration = 50;
	
	public VideoTrimmingServiceTest() {
		super(VideoTrimmingService.class);
	}

	public void setUp() throws Exception {
		super.setUp();
		setupService();
		context = getSystemContext();
		service = getService();
		assertNotNull(service);
	}

	public void tearDown() throws Exception {
		shutdownService();
		super.tearDown();
	}


	public void testBasicFunctionality() {
		Intent intent = getIntent(successMessenger());
		service.setVideoTrimmer(defaultTrimmer());
		startService(intent);
	}


	public void testErrorFunctionality() {
		Intent intent = getIntent(errorMessenger());
		service.setVideoTrimmer(faultyTrimmer());
		startService(intent);
	}

	private Intent getIntent(final Messenger messenger) {
		Intent intent = new Intent(context, VideoTrimmingService.class);
		intent.putExtra("inputFileName", inputFileName);
		intent.putExtra("outputFileName", outputFileName);
		intent.putExtra("start", startTime);
		intent.putExtra("duration", duration);
		intent.putExtra("messenger", messenger);
		return intent;
	}

	private Messenger successMessenger() {
		return new Messenger(new Handler(){
			@Override
			public void handleMessage(Message msg) {
				assertEquals("Trimmed video succesfully to output.3gp", msg.getData().getString("text"));
			}
		});
	}

	private Messenger errorMessenger() {
		return new Messenger(new Handler(){
			@Override
			public void handleMessage(Message msg) {
				assertEquals("Unable to trim the video. Check the error logs.", msg.getData().getString("text"));
			}
		});
	}

	private VideoTrimmer defaultTrimmer() {
		return new VideoTrimmer(){
			@Override
			public int trim_(String inputFile, String outFile, int start, int actualDuration) {
				assertFFMpegInput(inputFileName, outputFileName, startTime, duration, inputFile, outFile, start, actualDuration);
				return 0;
			}

		};
	}

	private VideoTrimmer faultyTrimmer() {
		return new VideoTrimmer(){
			@Override
			public int trim_(String inputFile, String outFile, int start, int actualDuration) {
				assertFFMpegInput(inputFileName, outputFileName, startTime, duration, inputFile, outFile, start, actualDuration);
				return 1;
			}

		};
	}

	private void assertFFMpegInput(final String inputFileName, final String outputFileName, final int startTime, final int duration, String inputFile, String outFile, int start, int actualDuration) {
		assertEquals(inputFileName, inputFile);
		assertEquals(outputFileName, outFile);
		assertEquals(startTime, start);
		assertEquals(duration, actualDuration);
	}

}
