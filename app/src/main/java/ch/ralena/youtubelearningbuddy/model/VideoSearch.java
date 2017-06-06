package ch.ralena.youtubelearningbuddy.model;

import java.util.ArrayList;
import java.util.List;

import ch.ralena.youtubelearningbuddy.model.video.Item;
import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;


public class VideoSearch {
	ReplaySubject<VideoSearch> notifier = ReplaySubject.create();

	List<Item> videos;

	public VideoSearch() {
		this.videos = new ArrayList<>();
	}

	public Observable<VideoSearch> asObservable() {
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
