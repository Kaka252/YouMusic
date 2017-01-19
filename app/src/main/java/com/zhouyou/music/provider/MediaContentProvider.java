package com.zhouyou.music.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.zhouyou.music.base.App;
import com.zhouyou.music.db.DBHelper;

/**
 * 作者：ZhouYou
 * 日期：2017/1/11.
 */
public class MediaContentProvider extends ContentProvider {

    private static final String AUTHORITY = "content://com.zhouyou.music.provider/";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/audio");
    private static final String TAG = "MediaContentProvider";

    private static final String TABLE = "audio";
    private ContentResolver resolver = null;
    private DBHelper dbHelper;

    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, "audio", 0);
        URI_MATCHER.addURI(AUTHORITY, "audio/#", 1);
    }

    private static final ArrayMap<String, String> articleProjectionMap;

    static {
        articleProjectionMap = new ArrayMap<>();
        articleProjectionMap.put("id", "id");
        articleProjectionMap.put("path", "id");
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: 创建Provider");
        resolver = getContext().getContentResolver();
        dbHelper = App.get().getHelper();
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case 0:
                return "audio";
            case 1:
                return "id";
            case 2:
                return "path";
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "onCreate: 查询");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        switch (URI_MATCHER.match(uri)) {
            case 0:
                sqlBuilder.setTables(TABLE);
                sqlBuilder.setProjectionMap(articleProjectionMap);
                break;
            case 1:
                String id = uri.getPathSegments().get(1);
                sqlBuilder.setTables(TABLE);
                sqlBuilder.setProjectionMap(articleProjectionMap);
                sqlBuilder.appendWhere("id=" + id);
                break;
            case 2:
                String path = uri.getPathSegments().get(2);
                sqlBuilder.setTables(TABLE);
                sqlBuilder.setProjectionMap(articleProjectionMap);
                sqlBuilder.appendWhere("path=" + path);
                break;
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
        Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, TextUtils.isEmpty(sortOrder) ? "id asc" : sortOrder, null);
        cursor.setNotificationUri(resolver, uri);
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Log.d(TAG, "onCreate: 插入数据");
        if (URI_MATCHER.match(uri) != 0) {
            throw new IllegalArgumentException("Error Uri: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(TABLE, "id", values);
        if (id < 0) {
            throw new SQLiteException("Unable to insert " + values + " for " + uri);
        }
        Uri newUri = ContentUris.withAppendedId(uri, id);
        resolver.notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "onCreate: 删除数据");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (URI_MATCHER.match(uri)) {
            case 0:
                count = db.delete(TABLE, selection, selectionArgs);
                break;
            case 1:
                String id = uri.getPathSegments().get(1);
                count = db.delete(TABLE, "id=" + id + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            case 2:
                String path = uri.getPathSegments().get(2);
                count = db.delete(TABLE, "path=" + path + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
        resolver.notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "onCreate: 更新数据");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (URI_MATCHER.match(uri)) {
            case 0:
                count = db.update(TABLE, values, selection, selectionArgs);
                break;
            case 1:
                String id = uri.getPathSegments().get(1);
                count = db.update(TABLE, values, "id=" + id + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            case 2:
                String path = uri.getPathSegments().get(2);
                count = db.update(TABLE, values, "path=" + path + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
        resolver.notifyChange(uri, null);
        return count;
    }
}
