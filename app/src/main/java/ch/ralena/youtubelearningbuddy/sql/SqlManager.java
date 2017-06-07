package ch.ralena.youtubelearningbuddy.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import ch.ralena.youtubelearningbuddy.object.Topic;
import ch.ralena.youtubelearningbuddy.object.TopicList;
import ch.ralena.youtubelearningbuddy.object.Video;

public class SqlManager {
	private static final String TAG = SqlManager.class.getSimpleName();

	private SqlHelper sqlHelper;

	public SqlManager(Context context) {
		sqlHelper = new SqlHelper(context);
	}

	public TopicList getTopicList() {
		SQLiteDatabase database = sqlHelper.getReadableDatabase();

		TopicList topicList = new TopicList();

		String col_name = SqlHelper.COL_TOPIC_NAME;

		Cursor cursor = database.query(
				SqlHelper.TABLE_TOPIC,
				new String[]{col_name, BaseColumns._ID},
				null,
				null,
				null,
				null,
				col_name + " ASC");

		if (cursor.moveToFirst()) {
			do {
				String name = getString(cursor, col_name);
				long id = getLong(cursor, BaseColumns._ID);
				Topic topic = new Topic(name);
				topic.setId(id);
				topic.setVideoList(getVideos(database, id));
				topicList.add(topic);
			} while (cursor.moveToNext());
		}

		return topicList;
	}

	public List<Video> getVideos(SQLiteDatabase database, long id) {
		List<Video> videoList = new ArrayList<>();

		Cursor videoCursor = database.rawQuery(
				"SELECT * FROM " + SqlHelper.TABLE_VIDEO +
						" WHERE " + SqlHelper.COL_VIDEO_FOREIGN_KEY_TOPIC + " = " + id +
						" ORDER BY " + SqlHelper.COL_VIDEO_POSITION + " ASC", null);
		if (videoCursor.moveToFirst()) {
			do {
				String publishedAt = getString(videoCursor, SqlHelper.COL_VIDEO_PUBLISHEDAT);
				String description = getString(videoCursor, SqlHelper.COL_VIDEO_DESCRIPTION);
				String title = getString(videoCursor, SqlHelper.COL_VIDEO_TITLE);
				String thumbnailUrl = getString(videoCursor, SqlHelper.COL_VIDEO_THUMBNAILURL);
				String videoId = getString(videoCursor, SqlHelper.COL_VIDEO_VIDEO_ID);
				int position = getInt(videoCursor, SqlHelper.COL_VIDEO_POSITION);

				Video video = new Video(publishedAt, title, description, thumbnailUrl, videoId, position);
				videoList.add(video);
			} while (videoCursor.moveToNext());
		}
		videoCursor.close();


		return videoList;
	}

	public long createTopic(Topic topic) {
		SQLiteDatabase database = sqlHelper.getWritableDatabase();
		database.beginTransaction();

		ContentValues topicListValues = new ContentValues();
		topicListValues.put(SqlHelper.COL_TOPIC_NAME, topic.getName());
		long id = database.insert(SqlHelper.TABLE_TOPIC, null, topicListValues);

		database.setTransactionSuccessful();
		database.endTransaction();

		database.close();
		return id;
	}

	public void addVideoToTopic(Video video, Topic topic, int position) {
		SQLiteDatabase database = sqlHelper.getWritableDatabase();
		database.beginTransaction();

		ContentValues videoValues = new ContentValues();
		videoValues.put(SqlHelper.COL_VIDEO_FOREIGN_KEY_TOPIC, topic.getId());
		videoValues.put(SqlHelper.COL_VIDEO_DESCRIPTION, video.getDescription());
		videoValues.put(SqlHelper.COL_VIDEO_TITLE, video.getTitle());
		videoValues.put(SqlHelper.COL_VIDEO_PUBLISHEDAT, video.getPublishedAt());
		videoValues.put(SqlHelper.COL_VIDEO_THUMBNAILURL, video.getThumbnailUrl());
		videoValues.put(SqlHelper.COL_VIDEO_VIDEO_ID, video.getId());
		videoValues.put(SqlHelper.COL_VIDEO_POSITION, position);

		database.insert(SqlHelper.TABLE_VIDEO, null, videoValues);

		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
	}

	private String getString(Cursor cursor, String columnName) {
		int index = cursor.getColumnIndex(columnName);
		return cursor.getString(index);
	}

	private long getLong(Cursor cursor, String columnName) {
		int index = cursor.getColumnIndex(columnName);
		return cursor.getLong(index);
	}

	private int getInt(Cursor cursor, String columnName) {
		int index = cursor.getColumnIndex(columnName);
		return cursor.getInt(index);
	}


}
