package ch.ralena.youtubelearningbuddy.api;

import ch.ralena.youtubelearningbuddy.model.SearchResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by crater on 02/06/17.
 */

public interface Youtube {
	String API_KEY = "AIzaSyBVk9X5d0onLjYxVaGZUQA_crLf6_wQOmU";
	String BASE_URL = "https://www.googleapis.com/youtube/v3/";


	// &order=rating&type=video&videoDefinition=high&videoEmbeddable=true&key=AIzaSyBVk9X5d0onLjYxVaGZUQA_crLf6_wQOmU

	@GET("search?part=snippet&key=" + API_KEY)
	Call<SearchResults> videos(
			@Query("order") String order,
			@Query("type") String type,        // video or playlist?
			@Query("q") String query,
			@Query("maxResults") int maxResults);
}
