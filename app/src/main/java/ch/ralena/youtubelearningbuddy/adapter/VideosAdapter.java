package ch.ralena.youtubelearningbuddy.adapter;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;

import ch.ralena.youtubelearningbuddy.R;
import ch.ralena.youtubelearningbuddy.object.VideoClickEvent;
import ch.ralena.youtubelearningbuddy.object.Topic;
import ch.ralena.youtubelearningbuddy.object.TopicList;
import ch.ralena.youtubelearningbuddy.object.Video;
import ch.ralena.youtubelearningbuddy.object.VideoList;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> implements Consumer<VideoList> {
	private static final String TAG = VideosAdapter.class.getSimpleName();

	private static final int VIEW_EMPTY = 0;
	private static final int VIEW_VIDEO = 1;
	private static final int MENU_TOPIC = 0;
	private static final int TOPIC_SUBMENU = 0;
	private static final int ITEM_TOPIC = 1;

	private VideoList videos;
	private TopicList topicList;
	private PublishSubject<VideoClickEvent> videoClickSubject = PublishSubject.create();

	public VideosAdapter(VideoList videos, TopicList topicList) {
		this.videos = videos;
		this.topicList = topicList;
	}

	public Observable<VideoClickEvent> asObservable() {
		return videoClickSubject;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == VIEW_VIDEO) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
			return new ViewHolder(view);
		} else {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_empty, parent, false);
			return new ViewHolderEmpty(view);
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		if (!videos.getVideos().isEmpty()) {
			Video video = videos.getVideos().get(position);
			VideoClickEvent videoClickEvent = new VideoClickEvent(
					video,
					holder.thumbnail,
					video.getId());
			holder.thumbnail.setTransitionName("item" + position);
			RxView.clicks(holder.container)
					.map(aVoid -> videoClickEvent)
					.subscribe(videoClickSubject);

			holder.bindView(videos.getVideos().get(position));
		}
	}


	@Override
	public int getItemCount() {
		return videos.getVideos().size() > 0 ? videos.getVideos().size() : 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (videos.getVideos().isEmpty()) {
			return VIEW_EMPTY;
		} else {
			return VIEW_VIDEO;
		}
	}

	@Override
	public void accept(@NonNull VideoList videoList) throws Exception {
		this.videos = videoList;
		notifyDataSetChanged();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		RelativeLayout container;
		TextView title;
		ImageView thumbnail;
		TextView optionsBtn;

		public ViewHolder(View itemView) {
			super(itemView);
			container = (RelativeLayout) itemView.findViewById(R.id.container);
			title = (TextView) itemView.findViewById(R.id.titleText);
			thumbnail = (ImageView) itemView.findViewById(R.id.thumbnailImage);
			optionsBtn = (TextView) itemView.findViewById(R.id.optionsButton);
		}


		public void bindView(Video video) {
			optionsBtn.setOnClickListener(view -> {
				PopupMenu popup = new PopupMenu(view.getContext(), view);
				SubMenu topicsMenu = popup.getMenu().addSubMenu(TOPIC_SUBMENU, MENU_TOPIC, Menu.NONE, "Add to topic");
				int itemId = 0;
				for (Topic topic : topicList.all()) {
					topicsMenu.add(ITEM_TOPIC, itemId++, Menu.NONE, topic.getName());
				}
				popup.setOnMenuItemClickListener(clickedItem -> {
					if (clickedItem.getGroupId() == ITEM_TOPIC) {
						boolean added = topicList.addVideoToTopic(clickedItem.getItemId(), video);
						if(added) {
							Toast.makeText(view.getContext(), "Video added to topic", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(view.getContext(), "You've already added this video", Toast.LENGTH_SHORT).show();
						}
					}
					return true;
				});
				popup.show();
			});

			title.setText(video.getTitle());
			Picasso.with(thumbnail.getContext())
					.load(video.getThumbnailUrl())
					.fit()
					.centerCrop()
					.into(thumbnail);
		}
	}

	private class ViewHolderEmpty extends ViewHolder {
		public ViewHolderEmpty(View view) {
			super(view);
		}
	}
}
