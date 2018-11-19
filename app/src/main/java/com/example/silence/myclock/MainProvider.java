package com.example.silence.myclock;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.silence.myclock.util.Logger;

public class MainProvider extends ContentProvider {
    private static final String AUTHORITY = "com.example.silence.myclock.MainProvider";
    private static final int MATCH_ALL_CODE = 0;
    private static final int MATCH_ONE_CODE = 1;
    private static UriMatcher uriMatcher;
    public static final Uri NOTIFY_URI = Uri.parse("content://" + AUTHORITY + "/labels");

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "labels", MATCH_ALL_CODE);
        uriMatcher.addURI(AUTHORITY, "labels/#", MATCH_ONE_CODE);
    }

    @Override
    public boolean onCreate() {
//        ContentO
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        Logger.finest("provider query");
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Logger.finest("provider insert");

        if (uriMatcher.match(uri) == MATCH_ALL_CODE) {
            Logger.finest("provider insert.");
            getContext().getContentResolver().notifyChange(MainProvider.NOTIFY_URI, null);
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Logger.finest("provider delete.");
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        Logger.finest("provider update.");
        return 0;
    }
}
