package ch.ralena.youtubelearningbuddy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.ralena.youtubelearningbuddy.R;

/**
 * Created by crater on 05/06/17.
 */

public class TopicsFragment extends Fragment {
	FloatingActionButton fab;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_topics, container, false);
		fab = (FloatingActionButton) view.findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		return view;
	}

	public static TopicsFragment newInstance() {
		TopicsFragment fragment = new TopicsFragment();
		// add bundle
		return fragment;
	}
}
