package ch.ralena.youtubelearningbuddy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import ch.ralena.youtubelearningbuddy.adapter.CommentsAdapter;
import ch.ralena.youtubelearningbuddy.api.YoutubeService;
import ch.ralena.youtubelearningbuddy.model.CommentList;
import ch.ralena.youtubelearningbuddy.model.SingleVideo;
import ch.ralena.youtubelearningbuddy.model.comment.CommentThreads;
import ch.ralena.youtubelearningbuddy.model.singleVideo.Snippet;
import ch.ralena.youtubelearningbuddy.model.singleVideo.VideoResult;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by crater on 04/06/17.
 */

public class VideoDetailActivity extends AppCompatActivity {
	private String videoId;
	CommentList comments;
	private SingleVideo singleVideo;

	private ImageView videoThumbnail;
	private TextView titleText;
	private TextView descriptionText;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videodetail);
		supportPostponeEnterTransition();

		// load views
		videoThumbnail = (ImageView) findViewById(R.id.videoThumbnail);
		titleText = (TextView) findViewById(R.id.title);
		descriptionText = (TextView) findViewById(R.id.description);

		// set up detail page objects
		comments = new CommentList();
		singleVideo = new SingleVideo();

		// get video id and load everything from Youtube
		videoId = getIntent().getStringExtra(MainActivity.VIDEO_ID);
		loadVideo();
		loadComments();

		// set up comments recycler view
		CommentsAdapter adapter = new CommentsAdapter();
		comments.asObservable().subscribe(adapter);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(adapter);
	}

	private void loadVideo() {
		singleVideo.asObservable()
				.subscribe(video -> {
					Picasso.with(videoThumbnail.getContext())
							.load(video.getThumbnailUrl())
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
					titleText.setText(video.getTitle());
					descriptionText.setText(video.getDescription());
				});

		YoutubeService.getYoutubeService()
				.video(videoId)
				.enqueue(new retrofit2.Callback<VideoResult>() {
					@Override
					public void onResponse(Call<VideoResult> call, Response<VideoResult> response) {
						Snippet snippet = response.body().getItem().get(0).getSnippet();
						singleVideo.loadFromSnippet(snippet);
					}

					@Override
					public void onFailure(Call<VideoResult> call, Throwable t) {

					}
				});
	}

	private void loadComments() {
		YoutubeService.getYoutubeService()
				.comments(videoId, 20)
				.enqueue(new retrofit2.Callback<CommentThreads>() {
					@Override
					public void onResponse(Call<CommentThreads> call, Response<CommentThreads> response) {
						if (response.isSuccessful()) {
							if (!response.body().getItems().isEmpty()) {
								comments.setComments(response.body());
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
