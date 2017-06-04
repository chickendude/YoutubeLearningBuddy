package ch.ralena.youtubelearningbuddy;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import ch.ralena.youtubelearningbuddy.adapter.VideosAdapter;
import ch.ralena.youtubelearningbuddy.api.YoutubeService;
import ch.ralena.youtubelearningbuddy.model.Item;
import ch.ralena.youtubelearningbuddy.model.SearchResults;
import ch.ralena.youtubelearningbuddy.model.VideoList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
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
		videosAdapter.asObservable().subscribe(new Action1<Item>() {
			@Override
			public void call(Item item) {
				Toast.makeText(MainActivity.this, item.getSnippet().getTitle(), Toast.LENGTH_SHORT).show();
			}
		});

		// set up recycler view
		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setAdapter(videosAdapter);

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
		getMenuInflater().inflate(R.menu.options, menu);

		// connect searchable config with SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		return true;
	}

}
