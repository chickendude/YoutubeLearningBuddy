package ch.ralena.youtubelearningbuddy.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import ch.ralena.youtubelearningbuddy.R;
import ch.ralena.youtubelearningbuddy.object.Topic;
import ch.ralena.youtubelearningbuddy.object.TopicList;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.ViewHolder> implements Consumer<TopicList> {
	TopicList topicList;
	PublishSubject<Topic> topicClickSubject = PublishSubject.create();

	public PublishSubject<Topic> asObservable() {
		return topicClickSubject;
	}

	public TopicsAdapter() {
		topicList = new TopicList();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.bindView(topicList.get(position));
	}

	@Override
	public int getItemCount() {
		return topicList.all().size();
	}

	@Override
	public void accept(@NonNull TopicList topicList) throws Exception {
		this.topicList = topicList;
		notifyDataSetChanged();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		private RelativeLayout container;
		private TextView topicName;
		private TextView numVideos;

		public ViewHolder(View itemView) {
			super(itemView);
			container = (RelativeLayout) itemView.findViewById(R.id.container);
			topicName = (TextView) itemView.findViewById(R.id.topicNameText);
			numVideos = (TextView) itemView.findViewById(R.id.videoCountText);
		}

		public void bindView(Topic topic) {
			topicName.setText(topic.getName());
			numVideos.setText("" + topic.getVideoList().size());
			RxView.clicks(container)
					.map(aVoid -> topic)
					.subscribe(topicClickSubject);

		}
	}
}
