
package ch.ralena.youtubelearningbuddy.model.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopLevelComment {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("etag")
    @Expose
    private String etag;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("snippet")
    @Expose
    private Snippet_ snippet;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Snippet_ getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet_ snippet) {
        this.snippet = snippet;
    }

}
