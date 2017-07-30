package com.falvojr.nd818.p2.data.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @see <a href="https://github.com/falvojr/ud851-Sunshine/blob/student/S12.04-Solution-ResourceQualifiers/app/src/main/java/com/example/android/sunshine/data/WeatherContract.java">Base on WeatherContract</a>
 */
public class TMDbContract {

    static final String CONTENT_AUTHORITY = "com.falvojr.nd818.p2";
    static final String PATH_TM_DB = "tmdb";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class TMDbEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TM_DB).build();
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_FAVORITE = "favorite";

        static final String TABLE_NAME = "movie";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}