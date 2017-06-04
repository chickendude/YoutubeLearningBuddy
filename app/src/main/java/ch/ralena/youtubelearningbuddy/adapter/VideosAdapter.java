package ch.ralena.youtubelearningbuddy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import java.util.List;

import ch.ralena.youtubelearningbuddy.R;
import ch.ralena.youtubelearningbuddy.model.Item;
import ch.ralena.youtubelearningbuddy.model.VideoList;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> implements Action1<VideoList> {
	private List<Item> videos;
	private PublishSubject<Item> videoClickSubject = PublishSubject.create();

	public VideosAdapter(List<Item> videos) {
		this.videos = videos;
	}

	public Observable<Item> asObservable() {
		return videoClickSubject;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final Item item = videos.get(position);

		RxView.clicks(holder.container)
				.map(new Func1<Void, Item>() {
					@Override
					public Item call(Void aVoid) {
						return item;
					}
				}).subscribe(videoClickSubject);

		holder.bindView(videos.get(position));
	}


	@Override
	public int getItemCount() {
		return videos.size();
	}

	@Override
	public void call(VideoList videoList) {
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
