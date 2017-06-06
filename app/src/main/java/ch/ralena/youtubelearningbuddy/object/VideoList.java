package ch.ralena.youtubelearningbuddy.object;

import java.util.ArrayList;
import java.util.List;

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

	public void setVideos(List<Video> videos) {
		this.videos = videos;
		notifier.onNext(this);
	}

	public void addVideos(List<Video> videos) {
		this.videos.addAll(videos);
		notifier.onNext(this);
	}

}
