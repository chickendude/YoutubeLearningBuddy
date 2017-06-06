package ch.ralena.youtubelearningbuddy.fragment;

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
import ch.ralena.youtubelearningbuddy.adapter.TopicsAdapter;
import ch.ralena.youtubelearningbuddy.object.Topic;
import ch.ralena.youtubelearningbuddy.object.TopicList;

public class TopicsFragment extends Fragment {
	private FloatingActionButton fab;
	private TopicList topicList;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_topics, container, false);

		topicList = new TopicList();

		fab = (FloatingActionButton) view.findViewById(R.id.fab);
		fab.setOnClickListener(v -> topicList.add(new Topic("Topic " + (topicList.getTopics().size() + 1))));

		// set up recycler view
		TopicsAdapter adapter = new TopicsAdapter();
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		topicList.asObservable().subscribe(adapter);
		return view;
	}

	public static TopicsFragment newInstance() {
		TopicsFragment fragment = new TopicsFragment();
		// add bundle
		return fragment;
	}
}
