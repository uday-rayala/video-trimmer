package net.video.trimmer.view;

import net.video.trimmer.R;
import net.video.trimmer.model.VideoPlayerState;
import net.video.trimmer.service.VideoTrimmingService;
import net.video.trimmer.util.TimeUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class ViewVideo extends Activity {
	protected final int LOADING_DIALOG = 1;
	protected final int MESSAGE_DIALOG = 2;
	protected final int VALIDATION_DIALOG = 3;

	private VideoView videoView;
	private TextView detailView;
	private VideoPlayerState videoPlayerState = new VideoPlayerState();
	private ProgressDialog dialog;

	
	protected Handler completionHander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.i("VideoView", "Recieved message");
			videoPlayerState.setMessageText(msg.getData().getString("text"));
			removeDialog(LOADING_DIALOG);
			showDialog(MESSAGE_DIALOG);
			stopService(videoTrimmingServiceIntent());
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("VideoView", "In on create");
		setContentView(R.layout.video_player);

		Object lastState = getLastNonConfigurationInstance();
		if (lastState != null) {
			videoPlayerState = (VideoPlayerState) lastState;
		} else {
			Bundle extras = getIntent().getExtras();
			videoPlayerState.setFilename(extras.getString("videofilename"));
		}

		videoView = (VideoView) findViewById(R.id.VideoView);
		videoView.setVideoPath(videoPlayerState.getFilename());

		MediaController mediaController = new MediaController(this);
		videoView.setMediaController(mediaController);
		videoView.requestFocus();
		videoView.start();

		Button startButton = (Button) findViewById(R.id.Start);
		startButton.setOnClickListener(startClickListener());

		Button stopButton = (Button) findViewById(R.id.Stop);
		stopButton.setOnClickListener(stopClickListener());

		Button resetButton = (Button) findViewById(R.id.Reset);
		resetButton.setOnClickListener(resetClickListener());

		Button trimButton = (Button) findViewById(R.id.Trim);
		trimButton.setOnClickListener(trimClickListener());

		detailView = (TextView) findViewById(R.id.Details);
		refreshDetailView();
	}

	
	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.i("VideoView", "In on retain");
		return videoPlayerState;
	}

	

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("VideoView", "In on resume");
		videoView.seekTo(videoPlayerState.getCurrentTime());	
	}


	@Override
	protected void onPause() {
		super.onPause();
		Log.i("VideoView", "In on pause");

		videoPlayerState.setCurrentTime(videoView.getCurrentPosition());
	}

	private OnClickListener trimClickListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				videoView.pause();
				
				if(!videoPlayerState.isValid()) {
					showDialog(VALIDATION_DIALOG);
					return;
				}
				Intent intent = videoTrimmingServiceIntent();

				intent.putExtra("inputFileName", videoPlayerState.getFilename());
				intent.putExtra("outputFileName", "/sdcard/trimmed.3gp");
				intent.putExtra("start", videoPlayerState.getStart()/1000);
				intent.putExtra("duration", videoPlayerState.getDuration()/1000);

				intent.putExtra("messenger", new Messenger(completionHander));
				
				startService(intent);
				showDialog(LOADING_DIALOG);
			}
		};
	}

	private Intent videoTrimmingServiceIntent() {
		return new Intent(ViewVideo.this, VideoTrimmingService.class);
	}
	
	private OnClickListener resetClickListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				videoPlayerState.reset();
				refreshDetailView();
			}
		};
	}

	private OnClickListener stopClickListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int stop = videoView.getCurrentPosition();
				videoPlayerState.setStop(stop);
				refreshDetailView();
			}
		};
	}

	private OnClickListener startClickListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				videoPlayerState
						.setStart(videoView.getCurrentPosition());
				refreshDetailView();
			}
		};
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		Log.i("VideoView", "In on prepare dialog");

		switch (id) {
		case MESSAGE_DIALOG:
			((AlertDialog) dialog).setMessage(videoPlayerState.getMessageText());
			break;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}


	@Override
	protected Dialog onCreateDialog(int id) {
		Log.i("VideoView", "In on create dialog");

		Dialog dialog;

		switch (id) {
		case VALIDATION_DIALOG:
			dialog = simpleAlertDialog("Invalid video timings selected for trimming. Please make sure your start time is less than the stop time."); 
			break;
		case LOADING_DIALOG:
			dialog = ProgressDialog.show(ViewVideo.this, "", "Trimming...",	true, true);
			break;
		case MESSAGE_DIALOG:
			dialog = simpleAlertDialog("");
			break;
		default:
			dialog = null;
		}

		return dialog;
	}


	private Dialog simpleAlertDialog(String message) {
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setCancelable(true)
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								ViewVideo.this.removeDialog(MESSAGE_DIALOG);
								ViewVideo.this.removeDialog(LOADING_DIALOG);
							}
						});
		dialog = builder.create();
		return dialog;
	}

	private void refreshDetailView() {
		String start = TimeUtils.toFormattedTime(videoPlayerState.getStart());
		String stop = TimeUtils.toFormattedTime(videoPlayerState.getStop());
		detailView.setText("Start at " + start + "\nEnd at " + stop);
	}
}