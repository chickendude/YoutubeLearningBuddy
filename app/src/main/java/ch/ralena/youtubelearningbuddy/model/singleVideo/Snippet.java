package ch.ralena.youtubelearningbuddy.model.singleVideo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by crater on 04/06/17.
 */

public class Snippet {
	@SerializedName("publishedAt")
	@Expose
	private String publishedAt;
	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("thumbnails")
	@Expose
	private Thumbnail thumbnail;

	public String getPublishedAt() {
		return publishedAt;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Thumbnail getThumbnail() {
		return thumbnail;
	}

	public class Thumbnail {
		@SerializedName("high")
		@Expose
		private High high;

		public High getHigh() {
			return high;
		}

		public class High {
			@SerializedName("url")
			@Expose
			private String url;

			public String getUrl() {
				return url;
			}
		}
	}
}
