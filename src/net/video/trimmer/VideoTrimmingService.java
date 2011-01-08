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
		VideoTrimmer.trim(inputFileName, outFileName, start, duration);
		try {
			Log.i("VideoTrimmerService", "Sending message");
			messenger.send(new Message());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
