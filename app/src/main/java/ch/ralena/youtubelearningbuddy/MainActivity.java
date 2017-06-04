package ch.ralena.youtubelearningbuddy;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ch.ralena.youtubelearningbuddy.adapter.VideosAdapter;
import ch.ralena.youtubelearningbuddy.api.YoutubeService;
import ch.ralena.youtubelearningbuddy.model.VideoList;
import ch.ralena.youtubelearningbuddy.model.video.Item;
import ch.ralena.youtubelearningbuddy.model.video.SearchResults;
import ch.ralena.youtubelearningbuddy.object.ItemClickEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ch.ralena.youtubelearningbuddy.R.menu.options;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	public static final String VIDEO_THUMBNAIL = "tag_video_thumbnail";
	public static final String TITLE = "tag_title";
	public static final String DESCRIPTION = "tag_description";
	public static final String TRANSITION_NAME = "tag_transition_name";
	public static final String VIDEO_ID = "tag_video_id";
	private RecyclerView recyclerView;
	private VideosAdapter videosAdapter;
	private VideoList videos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		videos = new VideoList();
		videosAdapter = new VideosAdapter(videos.getVideos());

		// subscribe our adapter to video list
		videos.asObservable().subscribe(videosAdapter);
		videosAdapter.asObservable().subscribe(itemClickEvent -> loadDetailActivity(itemClickEvent));

		// set up recycler view
		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setAdapter(videosAdapter);

	}

	private void loadDetailActivity(ItemClickEvent itemClickEvent) {
		Item item = itemClickEvent.getVideo();
		ImageView imageView = itemClickEvent.getImageView();
		Intent intent = new Intent(this, VideoDetailActivity.class);
		intent.putExtra(VIDEO_THUMBNAIL, item.getSnippet().getThumbnails().getHigh().getUrl());
		intent.putExtra(TITLE, item.getSnippet().getTitle());
		intent.putExtra(DESCRIPTION, item.getSnippet().getDescription());
		intent.putExtra(TRANSITION_NAME, imageView.getTransitionName());
		intent.putExtra(VIDEO_ID, itemClickEvent.getVideoId());
		ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, imageView.getTransitionName());
		startActivity(intent, options.toBundle());

	}

	@Override
	protected void onNewIntent(Intent intent) {
		String order = "rating";
		String type = "video";
		String query = "soymilk";
		int maxResults = 20;

		// check if we searched for something
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			query = intent.getStringExtra(SearchManager.QUERY);
		}

		TextView searchText = (TextView) findViewById(R.id.textView);
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
							Toast.makeText(MainActivity.this, "Error getting results", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Call<SearchResults> call, Throwable t) {
						Toast.makeText(MainActivity.this, "Error getting results", Toast.LENGTH_SHORT).show();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(options, menu);

		// connect searchable config with SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		return true;
	}

}
