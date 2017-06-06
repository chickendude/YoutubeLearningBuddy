package ch.ralena.youtubelearningbuddy.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by crater on 06/06/17.
 */

public class TopicList implements Parcelable {
	PublishSubject<TopicList> notifier = PublishSubject.create();
	private List<Topic> topics;

	protected TopicList(Parcel in) {
		in.readList(topics, null);
	}

	public static final Creator<TopicList> CREATOR = new Creator<TopicList>() {
		@Override
		public TopicList createFromParcel(Parcel in) {
			return new TopicList(in);
		}

		@Override
		public TopicList[] newArray(int size) {
			return new TopicList[size];
		}
	};

	public Observable<TopicList> asObservable() {
		return notifier;
	}

	public TopicList() {
		topics = new ArrayList<>();
	}

	public TopicList(List<Topic> topics) {
		this.topics = topics;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void add(Topic topic) {
		topics.add(topic);
		notifier.onNext(this);
	}

	public void addVideoToTopic(int topicId, Video video) {
		topics.get(topicId).getVideoList().addVideo(video);
		notifier.onNext(this);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(topics);
	}
}
