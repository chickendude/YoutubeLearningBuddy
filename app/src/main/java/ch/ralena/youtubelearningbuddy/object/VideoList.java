package ch.ralena.youtubelearningbuddy.object;

import java.util.ArrayList;
import java.util.List;

import ch.ralena.youtubelearningbuddy.model.video.Item;
import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;

public class VideoList {
	ReplaySubject<VideoList> notifier = ReplaySubject.create();

	List<Video> videos;

	public VideoList() {
		this.videos = new ArrayList<>();
	}

	public Observable<VideoList> asObservable() {
		return notifier;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideosFromItems(List<Item> items) {
		this.videos = new ArrayList<>();
		items.forEach(item -> videos.add(Video.loadFromItem(item)));
		notifier.onNext(this);
	}

	public void addVideos(List<Video> videos) {
		this.videos.addAll(videos);
		notifier.onNext(this);
	}

	public void addVideo(Video video) {
		this.videos.add(video);
		notifier.onNext(this);
	}
}
