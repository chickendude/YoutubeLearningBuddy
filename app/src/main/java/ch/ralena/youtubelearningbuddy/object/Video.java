package ch.ralena.youtubelearningbuddy.object;

import ch.ralena.youtubelearningbuddy.model.singleVideo.Snippet;
import ch.ralena.youtubelearningbuddy.model.video.Item;
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
	private String id;

	public Observable<Video> asObservable() {
		return notifier;
	}

	public void loadFromSnippet(Snippet snippet, String videoId) {
		this.publishedAt = snippet.getPublishedAt();
		this.title = snippet.getTitle();
		this.description = snippet.getDescription();
		this.thumbnailUrl = snippet.getThumbnail().getHigh().getUrl();
		this.id = videoId;
		notifier.onNext(this);
	}

	public static Video loadFromItem(Item item) {
		Video video = new Video();
		video.id = item.getId().getVideoId();
		video.publishedAt = item.getSnippet().getPublishedAt();
		video.title = item.getSnippet().getTitle();
		video.description = item.getSnippet().getDescription();
		video.thumbnailUrl = item.getSnippet().getThumbnails().getMedium().getUrl();
		return video;
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

	public String getId() {return id;}
}
