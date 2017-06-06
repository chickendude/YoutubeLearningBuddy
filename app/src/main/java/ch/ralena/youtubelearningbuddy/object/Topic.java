package ch.ralena.youtubelearningbuddy.object;

public class Topic {
	private String name;
	private VideoList videoList;

	public Topic(String name) {
		this.name = name;
		videoList = new VideoList();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VideoList getVideoList() {
		return videoList;
	}

	public void addVideo(Video video) {
		videoList.addVideo(video);
	}

	public void setVideoList(VideoList videoList) {
		this.videoList = videoList;
	}
}
