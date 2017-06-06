package ch.ralena.youtubelearningbuddy.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Topic implements Parcelable {
	private String name;
	private List<Video> videoList;

	public Topic(String name) {
		this.name = name;
		videoList = new ArrayList<>();
	}

	protected Topic(Parcel in) {
		name = in.readString();
		in.readList(videoList, null);
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeList(videoList);
	}
}
