package ch.ralena.youtubelearningbuddy.object;

import android.widget.TextView;

public class VideoClickEvent {
	private Video video;
	private TextView titleView;
	private String videoId;

	public VideoClickEvent(Video video, TextView titleView, String videoId) {
		this.video = video;
		this.titleView = titleView;
		this.videoId = videoId;
	}

	public Video getVideo() {
		return video;
	}

	public String getVideoId() {
		return videoId;
	}

	public TextView getTitleView() {
		return titleView;
	}
}
