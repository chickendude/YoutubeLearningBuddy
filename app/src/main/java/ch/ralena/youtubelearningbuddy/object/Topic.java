package ch.ralena.youtubelearningbuddy.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Topic implements Parcelable {
	private long id;
	private String name;
	private List<Video> videoList = new ArrayList<>();

	public Topic(String name) {
		this.name = name;
		videoList = new ArrayList<>();
	}

	protected Topic(Parcel in) {
		id = in.readLong();
		name = in.readString();
		in.readList(videoList, Topic.class.getClassLoader());
	}

	public static final Creator<Topic> CREATOR = new Creator<Topic>() {
		@Override
		public Topic createFromParcel(Parcel in) {
			return new Topic(in);
		}

		@Override
		public Topic[] newArray(int size) {
			return new Topic[size];
		}
	};

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Video> getVideoList() {
		return videoList;
	}

	public void addVideo(Video video) {
		videoList.add(video);
	}

	public void setVideoList(List<Video> videoList) {
		this.videoList = videoList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(name);
		dest.writeList(videoList);
	}
}
