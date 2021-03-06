package ch.ralena.youtubelearningbuddy.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class TopicList implements Parcelable {
	PublishSubject<TopicList> notifier = PublishSubject.create();
	private List<Topic> topics = new ArrayList<>();

	public TopicList() {
		topics = new ArrayList<>();
	}

	public TopicList(List<Topic> topics) {
		this.topics = topics;
	}

	private TopicList(Parcel in) {
		in.readList(topics, TopicList.class.getClassLoader());
	}


	public Observable<TopicList> asObservable() {
		return notifier;
	}


	public Topic get(int index) {
		return topics.get(index);
	}

	public void remove(Topic topic) {
		topics.remove(topic);
		notifier.onNext(this);
	}

	public List<Topic> all() {
		return topics;
	}

	public void add(Topic topic) {
		topics.add(topic);
		notifier.onNext(this);
	}

	public boolean addVideoToTopic(Topic topic, Video video) {
		if (!topic.getVideoList().contains(video)) {
			topic.addVideo(video);
			notifier.onNext(this);
			return true;
		} else {
			return false;
		}
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(topics);
	}
}
