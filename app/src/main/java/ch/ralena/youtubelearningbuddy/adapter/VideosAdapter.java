package ch.ralena.youtubelearningbuddy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ch.ralena.youtubelearningbuddy.R;
import ch.ralena.youtubelearningbuddy.model.Item;
import ch.ralena.youtubelearningbuddy.model.VideoList;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> implements Consumer<VideoList> {
	List<Item> videos;

	public VideosAdapter(List<Item> videos) {
		this.videos = videos;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
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
		TextView title;
		ImageView thumbnail;

		public ViewHolder(View itemView) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.titleText);
			thumbnail = (ImageView) itemView.findViewById(R.id.thumbnailImage);
		}


		public void bindView(Item item) {
			title.setText(item.getSnippet().getTitle());
			Picasso.with(thumbnail.getContext())
					.load(item.getSnippet().getThumbnails().getMedium().getUrl())
					.into(thumbnail);
		}
	}
}
