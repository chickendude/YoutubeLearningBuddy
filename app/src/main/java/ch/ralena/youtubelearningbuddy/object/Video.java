package ch.ralena.youtubelearningbuddy.object;

import android.os.Parcel;
import android.os.Parcelable;

import ch.ralena.youtubelearningbuddy.model.singleVideo.Snippet;
import ch.ralena.youtubelearningbuddy.model.video.Item;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by crater on 04/06/17.
 */

public class Video implements Parcelable {
	private PublishSubject<Video> notifier = PublishSubject.create();

	private String publishedAt;
	private String title;
	private String description;
	private String thumbnailUrl;
	private String id;
	private int position;

	public Video() {

	}

	public Video(String publishedAt, String title, String description, String thumbnailUrl, String id, int position) {
		this.publishedAt = publishedAt;
		this.title = title;
		this.description = description;
		this.thumbnailUrl = thumbnailUrl;
		this.id = id;
		this.position = position;
	}

	protected Video(Parcel in) {
		publishedAt = in.readString();
		title = in.readString();
		description = in.readString();
		thumbnailUrl = in.readString();
		id = in.readString();
		position = in.readInt();
	}

	public static final Creator<Video> CREATOR = new Creator<Video>() {
		@Override
		public Video createFromParcel(Parcel in) {
			return new Video(in);
		}

		@Override
		public Video[] newArray(int size) {
			return new Video[size];
		}
	};

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(publishedAt);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeString(thumbnailUrl);
		dest.writeString(id);
		dest.writeInt(position);
	}
}
