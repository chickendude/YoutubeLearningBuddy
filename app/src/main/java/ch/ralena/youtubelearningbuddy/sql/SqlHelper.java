package ch.ralena.youtubelearningbuddy.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by crater on 06/06/17.
 */

public class SqlHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "youtubelearningbuddy.db";
	private static final int DB_VERSION = 0;

	// SQL structure for Topics
	public static final String TABLE_TOPIC = "TOPIC";
	public static final String COL_TOPIC_NAME = "NAME";

	// SQL structure for Videos
	public static final String TABLE_VIDEO = "VIDEO";
	public static final String COL_VIDEO_INDEX = "INDEX";
	public static final String COL_VIDEO_PUBLISHEDAT = "PUBLISHEDAT";
	public static final String COL_VIDEO_TITLE = "TITLE";
	public static final String COL_VIDEO_DESCRIPTION = "DESCRIPTION";
	public static final String COL_VIDEO_THUMBNAILURL = "THUMBNAILURL";

	public SqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, DB_NAME, factory, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
