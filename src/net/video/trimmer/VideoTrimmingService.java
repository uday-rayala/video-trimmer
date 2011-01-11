package net.video.trimmer;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class VideoTrimmingService extends IntentService {

	public VideoTrimmingService() {
		super("VideoTrimmingService");
	}

	
	@Override
	public void onCreate() {
		super.onCreate();
		System.loadLibrary("ffmpeg");
		System.loadLibrary("video-trimmer");
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i("VideoTrimmerService", "On bind of service");
		Bundle extras = intent.getExtras();
		String inputFileName = extras.getString("inputFileName");
		String outFileName = extras.getString("outputFileName");
		int start = extras.getInt("start");
		int duration = extras.getInt("duration");
		
		Messenger messenger = (Messenger) extras.get("messenger");
		Log.i("VideoTrimmerService", "Starting trimming");
		System.gc();
		boolean error = false;
		
		try{
			int returnStatus = VideoTrimmer.trim(inputFileName, outFileName, start, duration);
			error = returnStatus != 0;
		} catch (Exception e) {
			error = true;
		}
		System.gc();
		
		String messageText = error ?"Unable to trim the video. Check the error logs.": "Trimmed video succesfully to "+outFileName;
		Log.i("VideoTrimmerService", "Sending message: "+messageText);
		

		try {
			
			Message message = new Message();
			message.getData().putString("text", messageText);
			messenger.send(message);
		} catch (RemoteException e) {
			Log.i("VideoTrimmerService", "Exception while sending message");
		}
	}

}
