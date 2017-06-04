package ch.ralena.youtubelearningbuddy.model.singleVideo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by crater on 04/06/17.
 */

public class Item {
	@SerializedName("snippet")
	@Expose
	private Snippet snippet;

	public Snippet getSnippet() {
		return snippet;
	}
}
