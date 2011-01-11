package net.video.trimmer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

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
		int video_column_index = videocursor
				.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
		String filename = videocursor.getString(video_column_index);
		return filename;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		System.gc();

		videocursor.moveToPosition(position);

		Context ctx = vContext.getApplicationContext();
		String fileName = getString(MediaStore.Video.Media.DISPLAY_NAME);
		Log.d("VideoAdapter", position + " "+fileName);
		String size = "Size(KB):" + getString(MediaStore.Video.Media.SIZE);
		String time = "Duration: " + toTime(getInt(MediaStore.Video.Media.DURATION));

		Bitmap thumbnail = Thumbnails.getThumbnail(vContext
				.getContentResolver(), getInt(MediaStore.Video.Media._ID),
				MediaStore.Video.Thumbnails.MICRO_KIND,
				new BitmapFactory.Options());

		ImageView imageView = new ImageView(ctx);
		imageView.setImageBitmap(thumbnail);

		LinearLayout det = new LinearLayout(ctx);
		det.setOrientation(LinearLayout.VERTICAL);
		det.addView(textView(ctx, fileName));
		det.addView(textView(ctx, size));
		det.addView(textView(ctx, time));

		LinearLayout linearLayout = new LinearLayout(ctx);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.addView(imageView);
		linearLayout.addView(det);
		linearLayout.setPadding(0, 5, 0, 5);
		return linearLayout;
	}

	private String toTime(int duration) {
		int seconds = duration / 1000;
		int minutes = seconds / 60;
		seconds -= (minutes * 60);
		int hours = minutes / 60;
		minutes -= (hours * 60);

		return toDigits(hours) + ":" + toDigits(minutes) + ":"
				+ toDigits(seconds);
	}

	private String toDigits(int num) {
		if (num > 9) {
			return String.valueOf(num);
		}
		return "0" + num;
	}

	private TextView textView(Context ctx, String text) {
		TextView textView = new TextView(ctx);
		textView.setText(text);
		textView.setPadding(10, 0, 0, 5);
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