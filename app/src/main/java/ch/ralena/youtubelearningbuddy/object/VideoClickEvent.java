package ch.ralena.youtubelearningbuddy.object;

import android.widget.ImageView;

/**
 * Created by crater on 04/06/17.
 */

public class VideoClickEvent {
	private Video video;
	private ImageView imageView;
	private String videoId;

	public VideoClickEvent(Video video, ImageView imageView, String videoId) {
		this.video = video;
		this.imageView = imageView;
		this.videoId = videoId;
	}

	public Video getVideo() {
		return video;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public String getVideoId() {
		return videoId;
	}
}
