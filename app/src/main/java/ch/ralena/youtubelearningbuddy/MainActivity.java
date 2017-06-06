package ch.ralena.youtubelearningbuddy;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;

import ch.ralena.youtubelearningbuddy.fragment.TopicsFragment;
import ch.ralena.youtubelearningbuddy.fragment.VideoSearchFragment;
import ch.ralena.youtubelearningbuddy.object.TopicList;

import static ch.ralena.youtubelearningbuddy.R.menu.options;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private ViewPager viewPager;
	private VideoSearchFragment videoSearchFragment;
	private TopicList topicList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		topicList = new TopicList();

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public CharSequence getPageTitle(int position) {
				switch (position) {
					case 0:
						return "Videos";
					default:
						return "Topics";
				}
			}

			@Override
			public Fragment getItem(int position) {
				switch (position) {
					case 0:
						videoSearchFragment = VideoSearchFragment.newInstance(topicList);
						return videoSearchFragment;
					default:
						return TopicsFragment.newInstance(topicList);
				}
			}

			@Override
			public int getCount() {
				return 2;
			}
		});
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
		tabLayout.setupWithViewPager(viewPager);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// check if we searched for something
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			viewPager.setCurrentItem(0);
			videoSearchFragment.newSearch(query);
		}
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
