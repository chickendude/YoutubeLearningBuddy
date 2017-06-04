package ch.ralena.youtubelearningbuddy.object;

import android.widget.ImageView;

import ch.ralena.youtubelearningbuddy.model.video.Item;

/**
 * Created by crater on 04/06/17.
 */

public class ItemClickEvent {
	private Item video;
	private ImageView imageView;
	private String videoId;

	public ItemClickEvent(Item video, ImageView imageView, String videoId) {
		this.video = video;
		this.imageView = imageView;
		this.videoId = videoId;
	}

	public Item getVideo() {
		return video;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public String getVideoId() {
		return videoId;
	}
}
