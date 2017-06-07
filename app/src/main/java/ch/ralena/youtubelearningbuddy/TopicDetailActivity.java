package ch.ralena.youtubelearningbuddy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import ch.ralena.youtubelearningbuddy.adapter.TopicVideosAdapter;
import ch.ralena.youtubelearningbuddy.fragment.TopicsFragment;
import ch.ralena.youtubelearningbuddy.object.Topic;

public class TopicDetailActivity extends AppCompatActivity {
	private static final String TAG = TopicDetailActivity.class.getSimpleName();
	private Topic topic;

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setContentView(R.layout.activity_topicdetail);

		topic = getIntent().getParcelableExtra(TopicsFragment.TOPIC);
		Log.d(TAG, topic.getName());
		setTitle(topic.getName());

		TopicVideosAdapter adapter = new TopicVideosAdapter();
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));

	}
}
