package ch.ralena.youtubelearningbuddy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import ch.ralena.youtubelearningbuddy.api.YoutubeService;
import ch.ralena.youtubelearningbuddy.model.CommentList;
import ch.ralena.youtubelearningbuddy.model.comment.CommentThreads;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by crater on 04/06/17.
 */

public class VideoDetailActivity extends AppCompatActivity {
	private String videoId;
	CommentList comments;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videodetail);
		supportPostponeEnterTransition();

		comments = new CommentList(null);
		videoId = getIntent().getStringExtra(MainActivity.VIDEO_ID);
		loadVideo();
		loadComments();
	}

	private void loadVideo() {
		String url = getIntent().getStringExtra(MainActivity.VIDEO_THUMBNAIL);
		String title = getIntent().getStringExtra(MainActivity.TITLE);
		String description = getIntent().getStringExtra(MainActivity.DESCRIPTION);
		String transitionName = getIntent().getStringExtra(MainActivity.TRANSITION_NAME);

		ImageView videoThumbnail = (ImageView) findViewById(R.id.videoThumbnail);
		TextView titleText = (TextView) findViewById(R.id.title);
		TextView descriptionText = (TextView) findViewById(R.id.description);

		videoThumbnail.setTransitionName(transitionName);

		Picasso.with(videoThumbnail.getContext())
				.load(url)
				.fit()
				.centerCrop()
				.into(videoThumbnail, new Callback() {
					@Override
					public void onSuccess() {
						supportStartPostponedEnterTransition();
					}

					@Override
					public void onError() {
						supportStartPostponedEnterTransition();
					}
				});
		titleText.setText(title);
		descriptionText.setText(description);
	}

	private void loadComments() {
		YoutubeService.getYoutubeService()
				.comments(videoId, 20)
				.enqueue(new retrofit2.Callback<CommentThreads>() {
					@Override
					public void onResponse(Call<CommentThreads> call, Response<CommentThreads> response) {
						if (response.isSuccessful()) {
							if (!response.body().getItems().isEmpty()) {
								comments = new CommentList(response.body().getItems());
							}
							if (comments.getComments().size() > 0) {
								Toast.makeText(
										VideoDetailActivity.this,
										comments.getComments().get(0).getTopLevelComment().getSnippet().getTextOriginal(),
										Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(VideoDetailActivity.this, "No comments", Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(VideoDetailActivity.this, "Error getting results", Toast.LENGTH_SHORT).show();
						}

					}

					@Override
					public void onFailure(Call<CommentThreads> call, Throwable t) {
						Toast.makeText(VideoDetailActivity.this, "Error getting results", Toast.LENGTH_SHORT).show();
					}
				});
	}

}
