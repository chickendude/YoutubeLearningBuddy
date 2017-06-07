package ch.ralena.youtubelearningbuddy.api;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import ch.ralena.youtubelearningbuddy.object.Video;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YoutubeService {
	public static Youtube getYoutubeService() {
		return new Retrofit.Builder()
				.baseUrl(Youtube.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build()
				.create(Youtube.class);
	}

	public static void openVideo(Context context, Video video) {
		String id = video.getId();
		Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
		Intent webIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://www.youtube.com/watch?v=" + id));
		try {
			context.startActivity(appIntent);
		} catch (ActivityNotFoundException ex) {
			context.startActivity(webIntent);
		}
	}
}
