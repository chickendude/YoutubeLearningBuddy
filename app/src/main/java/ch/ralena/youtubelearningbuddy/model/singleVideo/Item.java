package ch.ralena.youtubelearningbuddy.model.singleVideo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by crater on 04/06/17.
 */

public class Item {
	@SerializedName("id")
	@Expose
	private String id;

	@SerializedName("snippet")
	@Expose
	private Snippet snippet;

	public String getId() {
		return id;
	}

	public Snippet getSnippet() {
		return snippet;
	}
}
