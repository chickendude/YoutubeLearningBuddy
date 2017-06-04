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
import ch.ralena.youtubelearningbuddy.model.video.Item;
import ch.ralena.youtubelearningbuddy.model.VideoList;
import ch.ralena.youtubelearningbuddy.object.ItemClickEvent;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> implements Consumer<VideoList> {
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
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
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


	@Override
	public int getItemCount() {
		return videos.size();
	}

	@Override
	public void accept(@NonNull VideoList videoList) throws Exception {
		this.videos = videoList.getVideos();
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
}
