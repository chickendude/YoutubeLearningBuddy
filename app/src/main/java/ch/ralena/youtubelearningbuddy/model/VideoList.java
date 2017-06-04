package ch.ralena.youtubelearningbuddy.model;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.ReplaySubject;


public class VideoList {
	ReplaySubject<VideoList> notifier = ReplaySubject.create();

	List<Item> videos;

	public VideoList() {
		this.videos = new ArrayList<>();
	}

	public Observable<VideoList> asObservable() {
		return notifier;
	}

	public List<Item> getVideos() {
		return videos;
	}

	public void setVideos(List<Item> videos) {
		this.videos = videos;
		notifier.onNext(this);
	}

	public void addVideos(List<Item> videos) {
		this.videos.addAll(videos);
		notifier.onNext(this);
	}
}