package ch.ralena.youtubelearningbuddy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ch.ralena.youtubelearningbuddy.R;
import ch.ralena.youtubelearningbuddy.object.Topic;
import ch.ralena.youtubelearningbuddy.object.TopicList;
import ch.ralena.youtubelearningbuddy.sql.SqlManager;
import ch.ralena.youtubelearningbuddy.tools.Keyboard;

public class NewTopicDialogFragment extends DialogFragment {
	private SqlManager sqlManager;
	private EditText newTopicEdit;
	private Button button;

	TopicList topicList;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.topicList = getArguments().getParcelable(TopicsFragment.TOPIC_LIST);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_newtopic, container, false);
		sqlManager = new SqlManager(view.getContext());
		button = (Button) view.findViewById(R.id.createButton);
		newTopicEdit = (EditText) view.findViewById(R.id.newTopicEdit);
		newTopicEdit.setOnEditorActionListener((v, actionId, event) -> {
			if (actionId == 100) {
				button.performClick();
				return true;
			}
			return false;
		});
		button.setOnClickListener(v -> {
			Keyboard.close(view);
			String name = newTopicEdit.getText().toString();
			// there's an observable listener on topicList to update recycler view
			Topic topic = new Topic(name);
			topic.setId(sqlManager.createTopic(topic));
			topicList.add(topic);
			dismiss();
		});
		return view;
	}
}
