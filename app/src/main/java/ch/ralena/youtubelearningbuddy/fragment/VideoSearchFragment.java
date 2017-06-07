package ch.ralena.youtubelearningbuddy.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ch.ralena.youtubelearningbuddy.R;
import ch.ralena.youtubelearningbuddy.adapter.VideosAdapter;
import ch.ralena.youtubelearningbuddy.api.YoutubeService;
import ch.ralena.youtubelearningbuddy.model.video.SearchResults;
import ch.ralena.youtubelearningbuddy.object.TopicList;
import ch.ralena.youtubelearningbuddy.object.VideoClickEvent;
import ch.ralena.youtubelearningbuddy.object.VideoList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by crater on 05/06/17.
 */

public class VideoSearchFragment extends Fragment {
	private static final String TAG = VideoSearchFragment.class.getSimpleName();
	public static final String TRANSITION_NAME = "tag_transition_name";
	public static final String VIDEO_ID = "tag_video_id";
	public static final String TOPIC_LIST = "tag_topic_list";

	// views
	private RecyclerView recyclerView;
	private VideosAdapter videosAdapter;
	private TextView searchText;
	// member variables
	private VideoList videos;
	private TopicList topicList;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_videosearch, container, false);

		videos = new VideoList();
		topicList = getArguments().getParcelable(TOPIC_LIST);
		searchText = (TextView) view.findViewById(R.id.searchText);
		searchText.setText(R.string.no_search_results);
		videosAdapter = new VideosAdapter(videos, topicList);

		// subscribe our adapter to video list
		videos.asObservable().subscribe(videosAdapter);
		videosAdapter.asObservable().subscribe(videoClickEvent -> loadDetailFragment(videoClickEvent));

		// set up recycler view
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
		recyclerView.setAdapter(videosAdapter);

		return view;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu,inflater);
		inflater.inflate(R.menu.options, menu);

		// connect searchable config with SearchView
		SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
	}

	public static VideoSearchFragment newInstance(TopicList topicList) {
		VideoSearchFragment fragment = new VideoSearchFragment();
		// add bundle/arguments to fragment
		Bundle bundle = new Bundle();
		bundle.putParcelable(TOPIC_LIST, topicList);
		fragment.setArguments(bundle);
		return fragment;
	}

	private void loadDetailFragment(VideoClickEvent videoClickEvent) {
		getFragmentManager().beginTransaction()
				.replace(R.id.fragmentContainer, VideoDetailFragment.newInstance(topicList, videoClickEvent))
				.addToBackStack(null)
				.commit();
	}

	public void newSearch(String query) {
		String order = "rating";
		String type = "video";
		int maxResults = 20;

		searchText.setText("Search results for \"" + query + "\":");
		searchVideos(order, type, query, maxResults);
	}

	private void searchVideos(String order, String type, String query, int maxResults) {
		YoutubeService.getYoutubeService()
				.videos(order, type, query, maxResults)
				.enqueue(new Callback<SearchResults>() {
					@Override
					public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
						if (response.isSuccessful()) {
							videos.setVideosFromItems(response.body().getItems());
						} else {
							Toast.makeText(getActivity(), "Error getting results", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Call<SearchResults> call, Throwable t) {
						Toast.makeText(getActivity(), "Error getting results", Toast.LENGTH_SHORT).show();
					}
				});
	}

}
