/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.falvojr.nd818.p2.data.provider;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.falvojr.nd818.p2.data.provider.TMDbContract.TMDbEntry;

/**
 * @see <a href="https://github.com/falvojr/ud851-Sunshine/blob/student/S12.04-Solution-ResourceQualifiers/app/src/main/java/com/example/android/sunshine/data/WeatherProvider.java">Base on WeatherProvider</a>
 */
public class TMDbProvider extends ContentProvider {

    public static final int CODE_MOVIE = 100;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TMDbDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TMDbContract.CONTENT_AUTHORITY;

        /* This URI is content://com.falvojr.nd818.p2/movie/*/
        matcher.addURI(authority, TMDbContract.PATH_TM_DB, CODE_MOVIE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new TMDbDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        TMDbEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        throw new RuntimeException("I am not implementing getType in Project2.");
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("I am not implementing getType in Project2.");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri returnUri;
        long _id;
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                _id = mOpenHelper.getWritableDatabase().insert(TMDbEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = TMDbEntry.buildMovieUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("I am not implementing getType in Project2 (ON CONFLICT REPLACE).");
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}