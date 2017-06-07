package ch.ralena.youtubelearningbuddy.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import ch.ralena.youtubelearningbuddy.R;
import ch.ralena.youtubelearningbuddy.VideoDetailActivity;
import ch.ralena.youtubelearningbuddy.adapter.CommentsAdapter;
import ch.ralena.youtubelearningbuddy.api.YoutubeService;
import ch.ralena.youtubelearningbuddy.model.CommentList;
import ch.ralena.youtubelearningbuddy.model.comment.CommentThreads;
import ch.ralena.youtubelearningbuddy.model.singleVideo.Item;
import ch.ralena.youtubelearningbuddy.model.singleVideo.VideoResult;
import ch.ralena.youtubelearningbuddy.object.Topic;
import ch.ralena.youtubelearningbuddy.object.TopicList;
import ch.ralena.youtubelearningbuddy.object.Video;
import ch.ralena.youtubelearningbuddy.object.VideoClickEvent;
import ch.ralena.youtubelearningbuddy.sql.SqlManager;
import retrofit2.Call;
import retrofit2.Response;

import static ch.ralena.youtubelearningbuddy.fragment.VideoSearchFragment.TOPIC_LIST;
import static ch.ralena.youtubelearningbuddy.fragment.VideoSearchFragment.TRANSITION_NAME;
import static ch.ralena.youtubelearningbuddy.fragment.VideoSearchFragment.VIDEO_ID;

/**
 * Created by crater on 07/06/17.
 */

public class VideoDetailFragment extends Fragment {
	private static final int MAX_LINES = 2;
	private static final String TAG = VideoDetailActivity.class.getSimpleName();
	private static final int MENU_TOPIC = 0;
	private static final int TOPIC_SUBMENU = 1;
	private static final int ITEM_TOPIC = 2;

	private String videoId;
	private CommentList comments;
	private Video video;
	private TopicList topicList;

	private ImageView videoThumbnail;
	private TextView titleText;
	private TextView descriptionText;
	private TextView ellipsisText;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_videodetail, container, false);
//		supportPostponeEnterTransition();

		topicList = getArguments().getParcelable(TOPIC_LIST);

		// load views
		videoThumbnail = (ImageView) view.findViewById(R.id.videoThumbnail);
		videoThumbnail.setOnClickListener(v -> YoutubeService.openVideo(v.getContext(), video));
		String transitionName = getArguments().getString(TRANSITION_NAME);
		videoThumbnail.setTransitionName(transitionName);
		titleText = (TextView) view.findViewById(R.id.title);
		descriptionText = (TextView) view.findViewById(R.id.descriptionText);
		descriptionText.setMaxLines(MAX_LINES);
		ellipsisText = (TextView) view.findViewById(R.id.ellipsisText);
		descriptionText.setOnClickListener(v -> {
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
		videoId = getArguments().getString(VideoSearchFragment.VIDEO_ID);
		loadVideo();
		loadComments();

		// set up comments recycler view
		CommentsAdapter adapter = new CommentsAdapter();
		comments.asObservable().subscribe(adapter);
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(adapter);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		SubMenu topicsMenu = menu.addSubMenu(TOPIC_SUBMENU, MENU_TOPIC, Menu.NONE, "Add to topic");
		int itemId = 0;
		for (Topic topic : topicList.all()) {
			topicsMenu.add(ITEM_TOPIC, itemId++, Menu.NONE, topic.getName());
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getGroupId() == ITEM_TOPIC) {
			SqlManager sqlManager = new SqlManager(getContext());
			Topic topic = topicList.get(item.getItemId());
			boolean added = topicList.addVideoToTopic(topic, video);
			if (added) {
				sqlManager.addVideoToTopic(video, topic, topic.getVideoList().size());
				Toast.makeText(getContext(), "Video added to topic", Toast.LENGTH_SHORT).show();
				return true;
			} else {
				Toast.makeText(getContext(), "You've already added this video", Toast.LENGTH_SHORT).show();
			}
			return true;
		} else {
			return false;
		}
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
//									supportStartPostponedEnterTransition();
								}

								@Override
								public void onError() {
//									supportStartPostponedEnterTransition();
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

		YoutubeService.getYoutubeService()
				.video(videoId)
				.enqueue(new retrofit2.Callback<VideoResult>() {
					@Override
					public void onResponse(Call<VideoResult> call, Response<VideoResult> response) {
						Log.d(TAG, "on response");
						if (response.body() != null) {
							Item item = response.body().getItems().get(0);
							video.loadFromSnippet(item.getSnippet(), item.getId());
						}
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
								Toast.makeText(getActivity(), "No comments", Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(getActivity(), "Error getting results", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Call<CommentThreads> call, Throwable t) {
						Toast.makeText(getActivity(), "Failed: Error getting results", Toast.LENGTH_SHORT).show();
					}
				});
	}


	public static VideoDetailFragment newInstance(TopicList topicList, VideoClickEvent videoClickEvent) {
		VideoDetailFragment fragment = new VideoDetailFragment();
		// add bundle/arguments to fragment
		Bundle bundle = new Bundle();
		bundle.putParcelable(TOPIC_LIST, topicList);
		bundle.putString(VIDEO_ID, videoClickEvent.getVideoId());
		bundle.putString(TRANSITION_NAME, videoClickEvent.getImageView().getTransitionName());
		fragment.setArguments(bundle);
		return fragment;
	}
}
