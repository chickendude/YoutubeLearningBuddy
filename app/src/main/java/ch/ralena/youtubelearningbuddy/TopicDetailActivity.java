package ch.ralena.youtubelearningbuddy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import ch.ralena.youtubelearningbuddy.adapter.TopicVideosAdapter;
import ch.ralena.youtubelearningbuddy.fragment.TopicsFragment;
import ch.ralena.youtubelearningbuddy.object.Topic;

/**
 * Created by crater on 06/06/17.
 */

public class TopicDetailActivity extends AppCompatActivity {
	Topic topic;

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setContentView(R.layout.activity_topicdetail);

		topic = getIntent().getParcelableExtra(TopicsFragment.TOPIC);

		setTitle(topic.getName());

		TopicVideosAdapter adapter = new TopicVideosAdapter();
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));

	}
}
