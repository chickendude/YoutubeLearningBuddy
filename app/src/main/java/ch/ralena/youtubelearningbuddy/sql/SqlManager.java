package ch.ralena.youtubelearningbuddy.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ch.ralena.youtubelearningbuddy.object.Topic;
import ch.ralena.youtubelearningbuddy.object.TopicList;

/**
 * Created by crater on 06/06/17.
 */

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
				new String[]{col_name},
				null,
				null,
				null,
				null,
				col_name + " ASC");

		if (cursor.moveToFirst()) {
			do {
				String name = getString(cursor, col_name);
				Topic topic = new Topic(name);
				topicList.add(topic);
			} while (cursor.moveToNext());
		}

		return topicList;
	}

	public void createTopic(Topic topic) {
		SQLiteDatabase database = sqlHelper.getWritableDatabase();
		database.beginTransaction();

		ContentValues topicListValues = new ContentValues();
		topicListValues.put(SqlHelper.COL_TOPIC_NAME, topic.getName());
		database.insert(SqlHelper.TABLE_TOPIC, null, topicListValues);

		database.setTransactionSuccessful();
		database.endTransaction();

		database.close();
	}

	private String getString(Cursor cursor, String columnName) {
		int index = cursor.getColumnIndex(columnName);
		return cursor.getString(index);
	}


}
