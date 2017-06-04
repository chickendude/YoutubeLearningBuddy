
package ch.ralena.youtubelearningbuddy.model.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopLevelComment {

	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("snippet")
	@Expose
	private Comment comment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

}
