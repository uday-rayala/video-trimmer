package net.video.trimmer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

	private int start = 0, stop = 0;
	protected final int LOADING_DIALOG = 1;
	protected final int MESSAGE_DIALOG = 2;

	private VideoView videoView;
	private TextView detailView;
	private String messageText;
	

	protected Handler completionHander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.i("VideoVideo", "Recieved message");
			messageText = msg.getData().getString("text");
			dismissDialog(LOADING_DIALOG);
			showDialog(MESSAGE_DIALOG);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		System.gc();
		Bundle extras = getIntent().getExtras();
		filename = extras.getString("videofilename");

		RelativeLayout layout = new RelativeLayout(getApplicationContext());

		videoView = new VideoView(getApplicationContext());
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
				start = videoView.getCurrentPosition() / 1000;
				refreshDetailView();
			}
		});

		Button stopButton = new Button(getApplicationContext());
		stopButton.setText("Stop");
		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				stop = videoView.getCurrentPosition() / 1000;
				refreshDetailView();
			}
		});

		Button resetButton = new Button(getApplicationContext());
		resetButton.setText("Reset");
		resetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				start = stop = 0;
				refreshDetailView();
			}
		});

		Button trimButton = new Button(getApplicationContext());
		trimButton.setText("Trim");
		trimButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				videoView.pause();
				Intent intent = new Intent(ViewVideo.this,
						VideoTrimmingService.class);

				intent.putExtra("inputFileName", filename);
				intent.putExtra("outputFileName", "/sdcard/trimmed.3gp");
				intent.putExtra("start", start);
				intent.putExtra("duration", stop - start);
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
		super.onDestroy();
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch(id){
		case MESSAGE_DIALOG:
			((AlertDialog)dialog).setMessage(messageText);
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;

		switch (id) {
		case LOADING_DIALOG:
			dialog = ProgressDialog.show(ViewVideo.this, "", "Trimming...",
					true);
			break;
		case MESSAGE_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Hello...I am msg").setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                ViewVideo.this.dismissDialog(MESSAGE_DIALOG);
			           }
			       });
			dialog = builder.create();
			break;
		default:
			dialog = null;
		}

		return dialog;
	}

	private void refreshDetailView() {
		String text = "Start at " + start + "\nEnd at " + stop;
		detailView.setText(text);
	}
}