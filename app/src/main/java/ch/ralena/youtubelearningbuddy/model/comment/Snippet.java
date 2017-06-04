
package ch.ralena.youtubelearningbuddy.model.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snippet {

    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("topLevelComment")
    @Expose
    private TopLevelComment topLevelComment;
    @SerializedName("canReply")
    @Expose
    private boolean canReply;
    @SerializedName("totalReplyCount")
    @Expose
    private int totalReplyCount;
    @SerializedName("isPublic")
    @Expose
    private boolean isPublic;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public TopLevelComment getTopLevelComment() {
        return topLevelComment;
    }

    public void setTopLevelComment(TopLevelComment topLevelComment) {
        this.topLevelComment = topLevelComment;
    }

    public boolean isCanReply() {
        return canReply;
    }

    public void setCanReply(boolean canReply) {
        this.canReply = canReply;
    }

    public int getTotalReplyCount() {
        return totalReplyCount;
    }

    public void setTotalReplyCount(int totalReplyCount) {
        this.totalReplyCount = totalReplyCount;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

}
