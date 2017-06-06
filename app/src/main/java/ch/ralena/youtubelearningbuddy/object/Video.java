package ch.ralena.youtubelearningbuddy.object;

import ch.ralena.youtubelearningbuddy.model.singleVideo.Snippet;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by crater on 04/06/17.
 */

public class Video {
	private PublishSubject<Video> notifier = PublishSubject.create();

	private String publishedAt;
	private String title;
	private String description;
	private String thumbnailUrl;

	public Observable<Video> asObservable() {
		return notifier;
	}

	public void loadFromSnippet(Snippet snippet) {
		this.publishedAt = snippet.getPublishedAt();
		this.title = snippet.getTitle();
		this.description = snippet.getDescription();
		this.thumbnailUrl = snippet.getThumbnail().getHigh().getUrl();
		notifier.onNext(this);
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public String getPublishedAt() {
		return publishedAt;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
}