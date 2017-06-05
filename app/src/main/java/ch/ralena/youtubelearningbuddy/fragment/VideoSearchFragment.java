package ch.ralena.youtubelearningbuddy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ch.ralena.youtubelearningbuddy.R;
import ch.ralena.youtubelearningbuddy.VideoDetailActivity;
import ch.ralena.youtubelearningbuddy.adapter.VideosAdapter;
import ch.ralena.youtubelearningbuddy.api.YoutubeService;
import ch.ralena.youtubelearningbuddy.model.VideoList;
import ch.ralena.youtubelearningbuddy.model.video.SearchResults;
import ch.ralena.youtubelearningbuddy.object.ItemClickEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by crater on 05/06/17.
 */

public class VideoSearchFragment extends Fragment {
	public static final String TRANSITION_NAME = "tag_transition_name";
	public static final String VIDEO_ID = "tag_video_id";
	// views
	private RecyclerView recyclerView;
	private VideosAdapter videosAdapter;
	private TextView searchText;
	// member variables
	private VideoList videos;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_videosearch, container, false);

		videos = new VideoList();

		searchText = (TextView) view.findViewById(R.id.searchText);
		searchText.setText(R.string.no_search_results);
		videosAdapter = new VideosAdapter(videos.getVideos());

		// subscribe our adapter to video list
		videos.asObservable().subscribe(videosAdapter);
		videosAdapter.asObservable().subscribe(itemClickEvent -> loadDetailActivity(itemClickEvent));

		// set up recycler view
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setAdapter(videosAdapter);

		return view;
	}

	public static VideoSearchFragment newInstance() {
		VideoSearchFragment fragment = new VideoSearchFragment();
		// add bundle/arguments to fragment
		return fragment;
	}

	private void loadDetailActivity(ItemClickEvent itemClickEvent) {
		ImageView imageView = itemClickEvent.getImageView();
		Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
		intent.putExtra(VIDEO_ID, itemClickEvent.getVideoId());
		intent.putExtra(TRANSITION_NAME, imageView.getTransitionName());
		ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), imageView, imageView.getTransitionName());
		startActivity(intent, options.toBundle());
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
							videos.setVideos(response.body().getItems());
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
