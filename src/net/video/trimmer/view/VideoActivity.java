package net.video.trimmer.view;

import net.video.trimmer.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class VideoActivity extends Activity {
	Cursor videocursor;
	ListView videolist;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		String[] parameters = { MediaStore.Video.Media._ID,
				MediaStore.Video.Media.DATA,
				MediaStore.Video.Media.DISPLAY_NAME,
				MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION,
				MediaStore.Video.Media.DATE_ADDED,
				};

		videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				parameters, null, null, MediaStore.Video.Media.DATE_TAKEN
						+ " DESC");

		videolist = (ListView) findViewById(R.id.PhoneVideoList);

		ListAdapter resourceCursorAdapter = new VideoCursorAdapter(this, this, R.layout.video_preview, videocursor) ;
			
		videolist.setAdapter(resourceCursorAdapter);
		videolist.setOnItemClickListener(videogridlistener);
	}

	private OnItemClickListener videogridlistener = new OnItemClickListener() {
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			Cursor cursor = (Cursor) parent.getItemAtPosition(position);
			
			int fileNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
			String filename = cursor.getString(fileNameIndex );
			Intent intent = new Intent(VideoActivity.this, ViewVideo.class);
			intent.putExtra("videofilename", filename);
			startActivity(intent);
		}
	};
}
