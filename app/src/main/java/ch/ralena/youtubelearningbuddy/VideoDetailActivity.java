package ch.ralena.youtubelearningbuddy;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import ch.ralena.youtubelearningbuddy.adapter.CommentsAdapter;
import ch.ralena.youtubelearningbuddy.api.YoutubeService;
import ch.ralena.youtubelearningbuddy.fragment.VideoSearchFragment;
import ch.ralena.youtubelearningbuddy.model.CommentList;
import ch.ralena.youtubelearningbuddy.model.comment.CommentThreads;
import ch.ralena.youtubelearningbuddy.model.singleVideo.Item;
import ch.ralena.youtubelearningbuddy.model.singleVideo.VideoResult;
import ch.ralena.youtubelearningbuddy.object.TopicList;
import ch.ralena.youtubelearningbuddy.object.Video;
import retrofit2.Call;
import retrofit2.Response;

public class VideoDetailActivity extends AppCompatActivity {
	private static final int MAX_LINES = 2;
	private static final String TAG = VideoDetailActivity.class.getSimpleName();

	private String videoId;
	private CommentList comments;
	private Video video;
	private TopicList topicList;

	private ImageView videoThumbnail;
	private TextView titleText;
	private TextView descriptionText;
	private TextView ellipsisText;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videodetail);
		supportPostponeEnterTransition();

		topicList = getIntent().getParcelableExtra(VideoSearchFragment.TOPIC_LIST);

		// load views
		videoThumbnail = (ImageView) findViewById(R.id.videoThumbnail);
		String transitionName = getIntent().getStringExtra(VideoSearchFragment.TRANSITION_NAME);
		videoThumbnail.setTransitionName(transitionName);
		titleText = (TextView) findViewById(R.id.title);
		descriptionText = (TextView) findViewById(R.id.descriptionText);
		descriptionText.setMaxLines(MAX_LINES);
		ellipsisText = (TextView) findViewById(R.id.ellipsisText);
		descriptionText.setOnClickListener(view -> {
			ObjectAnimator animation;
			int numLines =
					descriptionText.getLineCount() == descriptionText.getMaxLines() ?
							MAX_LINES : descriptionText.getLineCount();
			animation = ObjectAnimator.ofInt(descriptionText, "maxLines", numLines);
			animation.setDuration(200).start();
		});

		// set up detail page objects
		comments = new CommentList();
		video = new Video();

		// get video id and load everything from Youtube
		videoId = getIntent().getStringExtra(VideoSearchFragment.VIDEO_ID);
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
		// update view when video data has loaded
		video.asObservable()
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
					// must set in code or else marquee won't work
					titleText.setSingleLine(true);
					titleText.setSelected(true);
					descriptionText.setText(video.getDescription());
					// check if description is too long
					if (descriptionText.getLineCount() <= MAX_LINES) {
						ellipsisText.setText("");
					}
				});

		Log.d(TAG, videoId);
		YoutubeService.getYoutubeService()
				.video(videoId)
				.enqueue(new retrofit2.Callback<VideoResult>() {
					@Override
					public void onResponse(Call<VideoResult> call, Response<VideoResult> response) {
						Log.d(TAG, "on response");
						Item item = response.body().getItems().get(0);
						video.loadFromSnippet(item.getSnippet(), item.getId());
					}

					@Override
					public void onFailure(Call<VideoResult> call, Throwable t) {
						Log.d(TAG, "on failure");
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
