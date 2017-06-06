package ch.ralena.youtubelearningbuddy.object;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by crater on 06/06/17.
 */

public class TopicList {
	PublishSubject<TopicList> notifer = PublishSubject.create();
	private List<Topic> topics;

	public Observable<TopicList> asObservable() {
		return notifer;
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
		notifer.onNext(this);
	}
}
