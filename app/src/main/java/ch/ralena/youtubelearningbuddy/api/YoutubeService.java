package ch.ralena.youtubelearningbuddy.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by crater on 02/06/17.
 */

public class YoutubeService {
	public static Youtube getYoutubeService() {
		return new Retrofit.Builder()
				.baseUrl(Youtube.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build()
				.create(Youtube.class);
	}
}
