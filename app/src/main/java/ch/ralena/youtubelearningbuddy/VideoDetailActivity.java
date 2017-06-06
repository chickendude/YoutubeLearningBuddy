package ch.ralena.youtubelearningbuddy;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import ch.ralena.youtubelearningbuddy.adapter.CommentsAdapter;
import ch.ralena.youtubelearningbuddy.api.YoutubeService;
import ch.ralena.youtubelearningbuddy.fragment.VideoSearchFragment;
import ch.ralena.youtubelearningbuddy.model.CommentList;
import ch.ralena.youtubelearningbuddy.model.SingleVideo;
import ch.ralena.youtubelearningbuddy.model.comment.CommentThreads;
import ch.ralena.youtubelearningbuddy.model.singleVideo.Snippet;
import ch.ralena.youtubelearningbuddy.model.singleVideo.VideoResult;
import retrofit2.Call;
import retrofit2.Response;

public class VideoDetailActivity extends AppCompatActivity {
	private static final int MAX_LINES = 2;

	private String videoId;
	CommentList comments;
	private SingleVideo singleVideo;

	private ImageView videoThumbnail;
	private TextView titleText;
	private TextView descriptionText;
	private TextView ellipsisText;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videodetail);
		supportPostponeEnterTransition();

		// load views
		videoThumbnail = (ImageView) findViewById(R.id.videoThumbnail);
		String transitionName = getIntent().getStringExtra(VideoSearchFragment.TRANSITION_NAME);
		videoThumbnail.setTransitionName(transitionName);
		titleText = (TextView) findViewById(R.id.title);
		descriptionText = (TextView) findViewById(R.id.description);
		ellipsisText = (TextView) findViewById(R.id.ellipsisText);
		descriptionText.setOnClickListener(view -> {
			ObjectAnimator animation;
			int numLines =
					descriptionText.getLineCount() == descriptionText.getMaxLines() ?
							MAX_LINES : descriptionText.getLineCount();
			checkDescriptionOverflow();
			animation = ObjectAnimator.ofInt(descriptionText, "maxLines", numLines);
			animation.setDuration(200).start();
		});

		// set up detail page objects
		comments = new CommentList();
		singleVideo = new SingleVideo();

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


	private void checkDescriptionOverflow() {
		// if it has more than 4 lines, check whether it's expanded or not
		// and update ellipsis text accordingly
		if(descriptionText.getLineCount() > MAX_LINES) {
			ellipsisText.setVisibility(View.VISIBLE);
			if (descriptionText.getLineCount() > descriptionText.getMaxLines()) {
				ellipsisText.setText("");
			} else {
				ellipsisText.setText("...");
			}
		}
	}

	private void loadVideo() {
		// update view when video data has loaded
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
					// must set in code or else marquee won't work
					titleText.setSingleLine(true);
					titleText.setSelected(true);
					descriptionText.setText(video.getDescription());
					// check if description is too long
					if(descriptionText.getLineCount() > MAX_LINES)
						descriptionText.setMaxLines(MAX_LINES);
						ellipsisText.setVisibility(View.VISIBLE);
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
