package ch.ralena.youtubelearningbuddy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import ch.ralena.youtubelearningbuddy.object.VideoList;

/**
 * Created by crater on 06/06/17.
 */

public class TopicVideosAdapter extends RecyclerView.Adapter<TopicVideosAdapter.ViewHolder> {
	VideoList videos;



	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return null;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

	}

	@Override
	public int getItemCount() {
		return 0;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View itemView) {
			super(itemView);
		}
	}
}
