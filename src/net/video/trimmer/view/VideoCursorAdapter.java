/**
 * 
 */
package net.video.trimmer.view;


import net.video.trimmer.R;
import net.video.trimmer.util.TimeUtils;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public final class VideoCursorAdapter extends ResourceCursorAdapter {
	VideoCursorAdapter(VideoActivity videoActivity, Context context, int layout, Cursor c) {
		super(context, layout, c);

	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ImageView videoPreview = (ImageView) view.findViewById(R.id.image_preview);
		Bitmap thumbnail = Thumbnails.getThumbnail(context.getContentResolver(), getInt(cursor, MediaStore.Video.Media._ID), MediaStore.Video.Thumbnails.MICRO_KIND,new BitmapFactory.Options());
		videoPreview.setImageBitmap(thumbnail);
		
		String fileName = getString(cursor, MediaStore.Video.Media.DISPLAY_NAME);
		Log.i("VideoCursorAdapter", "Binding view for : "+fileName);
		
		TextView fileNameView = (TextView) view.findViewById(R.id.file_name);
		fileNameView.setText(fileName);
		
		TextView durationView = (TextView) view.findViewById(R.id.duration);
		durationView.setText(getTime(cursor, MediaStore.Video.Media.DURATION));
		
		TextView addedOnView = (TextView) view.findViewById(R.id.added_date);
		addedOnView.setText(getString(cursor, MediaStore.Video.Media.DATE_ADDED));
	}

	private int getInt(Cursor cursor, String columnName) {
		int index = cursor.getColumnIndexOrThrow(columnName);
		return cursor.getInt(index);
	}

	private String getString(Cursor cursor, String columnName) {
		int index = cursor.getColumnIndexOrThrow(columnName);
		return cursor.getString(index);
	}
	
	private String getTime(Cursor cursor, String columnName) {
		int time = getInt(cursor, columnName);
		return TimeUtils.toFormattedTime(time);
	}
}