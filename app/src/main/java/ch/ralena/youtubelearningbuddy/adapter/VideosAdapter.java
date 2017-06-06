package ch.ralena.youtubelearningbuddy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;

import java.util.List;

import ch.ralena.youtubelearningbuddy.R;
import ch.ralena.youtubelearningbuddy.model.VideoSearch;
import ch.ralena.youtubelearningbuddy.model.video.Item;
import ch.ralena.youtubelearningbuddy.object.ItemClickEvent;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> implements Consumer<VideoSearch> {
	private static final int VIEW_EMPTY = 0;
	private static final int VIEW_VIDEO = 1;

	private List<Item> videos;
	private PublishSubject<ItemClickEvent> videoClickSubject = PublishSubject.create();

	public VideosAdapter(List<Item> videos) {
		this.videos = videos;
	}

	public Observable<ItemClickEvent> asObservable() {
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
		if (!videos.isEmpty()) {
			ItemClickEvent itemClickEvent = new ItemClickEvent(
					videos.get(position),
					holder.thumbnail,
					videos.get(position).getId().getVideoId());
			holder.thumbnail.setTransitionName("item" + position);
			RxView.clicks(holder.container)
					.map(aVoid -> itemClickEvent)
					.subscribe(videoClickSubject);

			holder.bindView(videos.get(position));
		}
	}


	@Override
	public int getItemCount() {
		return videos.size() > 0 ? videos.size() : 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (videos.isEmpty()) {
			return VIEW_EMPTY;
		} else {
			return VIEW_VIDEO;
		}
	}

	@Override
	public void accept(@NonNull VideoSearch videoSearch) throws Exception {
		this.videos = videoSearch.getVideos();
		notifyDataSetChanged();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		LinearLayout container;
		TextView title;
		ImageView thumbnail;

		public ViewHolder(View itemView) {
			super(itemView);
			container = (LinearLayout) itemView.findViewById(R.id.container);
			title = (TextView) itemView.findViewById(R.id.titleText);
			thumbnail = (ImageView) itemView.findViewById(R.id.thumbnailImage);
		}


		public void bindView(Item item) {
			title.setText(item.getSnippet().getTitle());
			Picasso.with(thumbnail.getContext())
					.load(item.getSnippet().getThumbnails().getMedium().getUrl())
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
