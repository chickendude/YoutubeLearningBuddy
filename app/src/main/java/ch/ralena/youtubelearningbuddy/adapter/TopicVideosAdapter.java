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
import ch.ralena.youtubelearningbuddy.object.Video;

/**
 * Created by crater on 06/06/17.
 */

public class TopicVideosAdapter extends RecyclerView.Adapter<TopicVideosAdapter.ViewHolder> {
	List<Video> videos;

	public TopicVideosAdapter(List<Video> videos) {
		this.videos = videos;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic_video, parent, false);
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

	public class ViewHolder extends RecyclerView.ViewHolder {
		private ImageView thumbnail;
		private TextView title;
		private TextView description;

		public ViewHolder(View itemView) {
			super(itemView);
			thumbnail = (ImageView) itemView.findViewById(R.id.thumbnailImage);
			title = (TextView) itemView.findViewById(R.id.titleText);
			description = (TextView) itemView.findViewById(R.id.descriptionText);
		}

		public void bindView(Video video) {
			title.setText(video.getTitle());
			description.setText(video.getDescription());
			Picasso.with(thumbnail.getContext())
					.load(video.getThumbnailUrl())
					.fit()
					.centerCrop()
					.into(thumbnail);
		}
	}
}
