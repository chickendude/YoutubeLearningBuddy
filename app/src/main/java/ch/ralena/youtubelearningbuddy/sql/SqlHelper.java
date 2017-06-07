package ch.ralena.youtubelearningbuddy.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by crater on 06/06/17.
 */

public class SqlHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "youtubelearningbuddy.db";
	private static final int DB_VERSION = 2;

	// SQL structure for Topics
	public static final String TABLE_TOPIC = "TOPIC";
	public static final String COL_TOPIC_NAME = "NAME";

	// SQL structure for Videos
	public static final String TABLE_VIDEO = "VIDEO";
	public static final String COL_VIDEO_POSITION = "POSITION";
	public static final String COL_VIDEO_PUBLISHEDAT = "PUBLISHEDAT";
	public static final String COL_VIDEO_TITLE = "TITLE";
	public static final String COL_VIDEO_DESCRIPTION = "DESCRIPTION";
	public static final String COL_VIDEO_THUMBNAILURL = "THUMBNAILURL";
	public static final String COL_VIDEO_VIDEO_ID = "VIDEO_ID";
	public static final String COL_VIDEO_FOREIGN_KEY_TOPIC = "TOPIC_ID";

	// SQL statements
	private static final String CREATE_TOPIC =
			"CREATE TABLE " + TABLE_TOPIC +
					"( " + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COL_TOPIC_NAME + " TEXT )";
	private static final String CREATE_VIDEO =
			"CREATE TABLE " + TABLE_VIDEO +
					"( " + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COL_VIDEO_POSITION + " INTEGER, " +
					COL_VIDEO_PUBLISHEDAT + " TEXT, " +
					COL_VIDEO_TITLE + " TEXT, " +
					COL_VIDEO_DESCRIPTION + " TEXT, " +
					COL_VIDEO_THUMBNAILURL + " TEXT, " +
					COL_VIDEO_VIDEO_ID + " TEXT, " +
					COL_VIDEO_FOREIGN_KEY_TOPIC + " INTEGER, " +
					"FOREIGN KEY(" + COL_VIDEO_FOREIGN_KEY_TOPIC + ") REFERENCES " + TABLE_TOPIC + "(_ID)" +
					" )";

	// Updates
	private static final String ADD_VIDEO_ID_COLUMN =
			"ALTER TABLE " + TABLE_VIDEO + " ADD " + COL_VIDEO_VIDEO_ID + " TEXT ";



	public SqlHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TOPIC);
		db.execSQL(CREATE_VIDEO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch(oldVersion) {
			case 1:
				db.execSQL(ADD_VIDEO_ID_COLUMN);
				break;
		}
	}
}
