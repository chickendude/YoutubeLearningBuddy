package ch.ralena.youtubelearningbuddy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by crater on 04/06/17.
 */

public class VideoDetailActivity extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videodetail);

		String url = getIntent().getStringExtra(MainActivity.VIDEO_THUMBNAIL);
		String title = getIntent().getStringExtra(MainActivity.TITLE);
		String description = getIntent().getStringExtra(MainActivity.DESCRIPTION);

		ImageView videoThumbnail = (ImageView) findViewById(R.id.videoThumbnail);
		TextView titleText = (TextView) findViewById(R.id.title);
		TextView descriptionText = (TextView) findViewById(R.id.description);

		Picasso.with(videoThumbnail.getContext())
				.load(url)
				.fit()
				.centerCrop()
				.into(videoThumbnail);
		titleText.setText(title);
		descriptionText.setText(description);
	}
}
