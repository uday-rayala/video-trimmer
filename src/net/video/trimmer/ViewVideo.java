package net.video.trimmer;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.RelativeLayout.LayoutParams;

public class ViewVideo extends Activity {
	private String filename;
	private TextView detailView;
	private int start, stop;
	private int duration;
	protected int LOADING_DIALOG = 1;
	protected Handler completionHander = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Log.i("VideoVideo", "Recieved message");
			removeDialog(LOADING_DIALOG);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.gc();
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		filename = extras.getString("videofilename");
		
		RelativeLayout layout = new RelativeLayout(getApplicationContext());
		
		final VideoView videoView = new VideoView(getApplicationContext());
		videoView.setVideoPath(filename);
		
		MediaController mediaController = new MediaController(this);
		videoView.setMediaController(mediaController);
		videoView.requestFocus();
		videoView.start();
		
		layout.addView(videoView, LayoutParams.FILL_PARENT);
		
		Button startButton = new Button(getApplicationContext());
		startButton.setText("Start");
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				start = videoView.getCurrentPosition()/1000;
				duration = videoView.getDuration()/1000;
				refreshDetailView();
			}
		});
		
		Button stopButton = new Button(getApplicationContext());
		stopButton.setText("Stop");
		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				stop = videoView.getCurrentPosition()/1000;
				duration = videoView.getDuration()/1000;
				refreshDetailView();
			}
		});
		
		Button resetButton = new Button(getApplicationContext());
		resetButton.setText("Reset");
		resetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				start = stop = 0;
				duration = videoView.getDuration()/1000;
				refreshDetailView();
			}
		});
		
		Button trimButton = new Button(getApplicationContext());
		trimButton.setText("Trim");
		trimButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				videoView.pause();
				Intent intent = new Intent(ViewVideo.this, VideoTrimmingService.class);

				intent.putExtra("inputFileName", filename);
				intent.putExtra("outputFileName", "/sdcard/trimmed.3gp");
				intent.putExtra("start", start);
				intent.putExtra("duration", stop-start);
				intent.putExtra("messenger", new Messenger(completionHander));

				startService(intent);
				showDialog(LOADING_DIALOG);
			}
		});
		
		detailView = new TextView(getApplicationContext());
		detailView.setTextColor(Color.RED);
		refreshDetailView();
		
		LinearLayout buttons = new LinearLayout(getApplicationContext());
		buttons.addView(startButton);
		buttons.addView(stopButton);
		buttons.addView(resetButton);
		buttons.addView(trimButton);
		buttons.addView(detailView);
		layout.addView(buttons);
		
		setContentView(layout);
	}

	@Override
	protected void onDestroy() {
		stopService(new Intent(this, VideoTrimmingService.class));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		ProgressDialog dialog = ProgressDialog.show(ViewVideo.this, "", "Trimming...", true);
		return dialog;
	}

	private void refreshDetailView() {
		String text = "Start at "+start+ "\nEnd at "+stop;
		detailView.setText(text);
	}
}