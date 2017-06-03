package ch.ralena.youtubelearningbuddy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.ralena.youtubelearningbuddy.R;
import ch.ralena.youtubelearningbuddy.model.Item;

/**
 * Created by crater on 02/06/17.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {
	ArrayList<Item> videos;

	public VideosAdapter(ArrayList<Item> videos) {
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

	public void updateVideos(ArrayList<Item> videos) {
		this.videos = videos;
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
		}
	}
}
