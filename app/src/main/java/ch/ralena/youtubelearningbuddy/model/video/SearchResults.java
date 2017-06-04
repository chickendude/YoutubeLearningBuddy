
package ch.ralena.youtubelearningbuddy.model.video;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResults {

	@SerializedName("nextPageToken")
	@Expose
	private String nextPageToken;
	@SerializedName("pageInfo")
	@Expose
	private PageInfo pageInfo;
	@SerializedName("items")
	@Expose
	private List<Item> items = null;

	public String getNextPageToken() {
		return nextPageToken;
	}

	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

}
