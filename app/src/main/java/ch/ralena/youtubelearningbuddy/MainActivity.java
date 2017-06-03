package ch.ralena.youtubelearningbuddy;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import java.util.ArrayList;

import ch.ralena.youtubelearningbuddy.adapter.VideosAdapter;
import ch.ralena.youtubelearningbuddy.api.YoutubeService;
import ch.ralena.youtubelearningbuddy.model.Item;
import ch.ralena.youtubelearningbuddy.model.SearchResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private RecyclerView recyclerView;
	private VideosAdapter videosAdapter;
	private ArrayList<Item> videos;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);

		// connect searchable config with SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		videos = new ArrayList<>();

		String order = "rating";
		String type = "video";
		String query = "soymilk";
		int maxResults = 20;

		YoutubeService.getYoutubeService()
				.videos(order, type, query, maxResults)
				.enqueue(new Callback<SearchResults>() {
					@Override
					public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
						if (response.isSuccessful()) {
							Log.d(TAG, "success");
							videos = (ArrayList<Item>) response.body().getItems();
							videosAdapter.updateVideos(videos);
						} else {
							Toast.makeText(MainActivity.this, "Error getting results", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Call<SearchResults> call, Throwable t) {
						Toast.makeText(MainActivity.this, "Error getting results", Toast.LENGTH_SHORT).show();
					}
				});

		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
		videosAdapter = new VideosAdapter(videos);
		recyclerView.setAdapter(videosAdapter);

	}
}
