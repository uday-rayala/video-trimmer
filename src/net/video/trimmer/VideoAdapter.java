package net.video.trimmer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VideoAdapter extends BaseAdapter {
	/**
	 * 
	 */
	private final Cursor videocursor;
	private Context vContext;

	public VideoAdapter(Cursor videocursor, Context c) {
		this.videocursor = videocursor;
		vContext = c;
	}

	public int getCount() {
		return videocursor.getCount();
	}

	public Object getItem(int position) {
		videocursor.moveToPosition(position);
		int video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
		return videocursor.getString(video_column_index);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null)
			return convertView;

		System.gc();

		videocursor.moveToPosition(position);

		Context ctx = vContext.getApplicationContext();
		String fileName = getString(MediaStore.Video.Media.DISPLAY_NAME);
		String details = " Size(KB):" + getString(MediaStore.Video.Media.SIZE);

		Bitmap thumbnail = Thumbnails.getThumbnail(vContext.getContentResolver(), getInt(MediaStore.Video.Media._ID), MediaStore.Video.Thumbnails.MICRO_KIND, new BitmapFactory.Options());

		ImageView imageView = new ImageView(ctx);
		imageView.setImageBitmap(thumbnail);
		
		LinearLayout det = new LinearLayout(ctx);
		det.setOrientation(LinearLayout.VERTICAL);
		det.addView(textView(ctx, fileName));
		det.addView(textView(ctx, details));

		LinearLayout linearLayout = new LinearLayout(ctx);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.addView(imageView);
		linearLayout.addView(det);
		return linearLayout;
	}

	private TextView textView(Context ctx, String text) {
		TextView textView = new TextView(ctx);
		textView.setText(text);
		return textView;
		
	}

	private String getString(String columnName) {
		int index = videocursor.getColumnIndexOrThrow(columnName);
		return videocursor.getString(index);
	}

	private int getInt(String columnName) {
		int index = videocursor.getColumnIndexOrThrow(columnName);
		return videocursor.getInt(index);
	}
}