package ch.ralena.youtubelearningbuddy.model;

import java.util.ArrayList;
import java.util.List;

import ch.ralena.youtubelearningbuddy.model.comment.CommentThreads;
import ch.ralena.youtubelearningbuddy.model.comment.Snippet;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by crater on 04/06/17.
 */

public class CommentList {
	PublishSubject<CommentList> notifier = PublishSubject.create();

	List<Snippet> comments = new ArrayList<>();

	public Observable<CommentList> asObservable() {
		return notifier;
	}

	public void setComments(CommentThreads commentThreads) {
		comments = new ArrayList<>();
		if (commentThreads != null) {
			commentThreads.getItems().forEach(item -> comments.add(item.getSnippet()));
		}
		notifier.onNext(this);
	}

	public List<Snippet> getComments() {
		return comments;
	}
}
