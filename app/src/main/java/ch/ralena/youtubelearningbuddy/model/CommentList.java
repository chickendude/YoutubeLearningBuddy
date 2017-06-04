package ch.ralena.youtubelearningbuddy.model;

import java.util.ArrayList;
import java.util.List;

import ch.ralena.youtubelearningbuddy.model.comment.Item;
import ch.ralena.youtubelearningbuddy.model.comment.Snippet;

/**
 * Created by crater on 04/06/17.
 */

public class CommentList {
	List<Snippet> comments;

	public CommentList(List<Item> comments) {
		this.comments = new ArrayList<>();
		if (comments != null) {
			comments.forEach(item -> getComments().add(item.getSnippet()));
		}
	}

	public List<Snippet> getComments() {
		return comments;
	}
}
