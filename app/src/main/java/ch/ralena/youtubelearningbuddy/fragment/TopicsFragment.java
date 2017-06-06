package ch.ralena.youtubelearningbuddy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.ralena.youtubelearningbuddy.R;
import ch.ralena.youtubelearningbuddy.TopicDetailActivity;
import ch.ralena.youtubelearningbuddy.adapter.TopicsAdapter;
import ch.ralena.youtubelearningbuddy.object.Topic;
import ch.ralena.youtubelearningbuddy.object.TopicList;
import ch.ralena.youtubelearningbuddy.tools.Keyboard;

public class TopicsFragment extends Fragment {
	private static final String TAG = TopicsFragment.class.getSimpleName();
	public static final String TOPIC_LIST = "tag_topic_list";
	public static final String TOPIC = "tag_topic";
	private FloatingActionButton fab;
	private TopicList topicList;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_topics, container, false);

		topicList = getArguments().getParcelable(TOPIC_LIST);


		fab = (FloatingActionButton) view.findViewById(R.id.fab);
		fab.setOnClickListener(v -> {
			Keyboard.open(v);
			// create bundle
			Bundle bundle = new Bundle();
			bundle.putParcelable(TOPIC_LIST, topicList);
			// create dialog fragment
			NewTopicDialogFragment dialogFragment = new NewTopicDialogFragment();
			dialogFragment.setArguments(bundle);
			dialogFragment.show(getFragmentManager(), null);
		});

		// set up recycler view
		TopicsAdapter adapter = new TopicsAdapter();
		adapter.asObservable().subscribe(this::loadTopicDetailActivity);
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		topicList.asObservable().subscribe(adapter);
		return view;
	}

	public static TopicsFragment newInstance(TopicList topicList) {
		TopicsFragment fragment = new TopicsFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(TOPIC_LIST, topicList);
		fragment.setArguments(bundle);
		return fragment;
	}

	private void loadTopicDetailActivity(Topic topic) {
		Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
		intent.putExtra(TOPIC, topic);
		startActivity(intent);
	}
}
