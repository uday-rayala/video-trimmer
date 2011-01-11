package net.video.trimmer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class VideoActivity extends Activity {
	Cursor videocursor;
	ListView videolist;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		String[] parameters = { 
				MediaStore.Video.Media._ID,
				MediaStore.Video.Media.DATA,
				MediaStore.Video.Media.DISPLAY_NAME,
				MediaStore.Video.Media.SIZE, 
				MediaStore.Video.Media.DURATION,
		};
		
		videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, parameters, null, null, MediaStore.Video.Media.DATE_TAKEN +" DESC");
		videolist = (ListView) findViewById(R.id.PhoneVideoList);
		videolist.setAdapter(new VideoAdapter(this.videocursor, getApplicationContext()));
		videolist.setOnItemClickListener(videogridlistener);
	}

	private OnItemClickListener videogridlistener = new OnItemClickListener() {
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			String filename = (String) parent.getItemAtPosition(position);
			Intent intent = new Intent(VideoActivity.this, ViewVideo.class);
			intent.putExtra("videofilename", filename);
			startActivity(intent);
		}
	};
}
