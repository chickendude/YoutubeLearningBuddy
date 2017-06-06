package ch.ralena.youtubelearningbuddy.model.singleVideo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResult {
	@SerializedName("items")
	@Expose
	private List<Item> items;

	public List<Item> getItems() {
		return items;
	}
}
