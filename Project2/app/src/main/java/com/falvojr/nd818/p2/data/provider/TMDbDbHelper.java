package com.falvojr.nd818.p2.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.falvojr.nd818.p2.data.provider.TMDbContract.TMDbEntry;

/**
 * @see <a href="https://github.com/falvojr/ud851-Sunshine/blob/student/S12.04-Solution-ResourceQualifiers/app/src/main/java/com/example/android/sunshine/data/WeatherDbHelper.java">Base on WeatherDbHelper</a>
 */
public class TMDbDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tmdb.db";

    private static final int DATABASE_VERSION = 1;

    public TMDbDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WEATHER_TABLE =
                "CREATE TABLE " + TMDbEntry.TABLE_NAME + " (" +
                        TMDbEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TMDbEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                        TMDbEntry.COLUMN_FAVORITE + " INTEGER DEFAULT 0," +
                        "UNIQUE (" + TMDbEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TMDbEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}