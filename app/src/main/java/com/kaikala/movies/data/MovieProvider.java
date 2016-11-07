package com.kaikala.movies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by kaikala on 10/8/16.
 */

public class MovieProvider extends ContentProvider {
    private static final String LOG = MovieProvider.class.getSimpleName();
    private static final UriMatcher URI_MATCHER = buildUriMatcher();
    private MovieDataBaseHelper movieDataBaseHelper;
    static final int MOVIE = 100;
    static final int MOVIE_ID = 200;

    static UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, MovieContract.MOVIE_PATH, MOVIE);
        uriMatcher.addURI(authority, MovieContract.MOVIE_PATH + "/#", MOVIE_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        movieDataBaseHelper = new MovieDataBaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (URI_MATCHER.match(uri)) {
            case MOVIE:
                cursor = movieDataBaseHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;
            case MOVIE_ID:
                cursor = movieDataBaseHelper.getReadableDatabase().query(MovieContract.MovieEntry.COLUMN_MOVIE_ID, projection, MovieContract.MovieEntry.COLUMN_MOVIE_ID + " =?" , selectionArgs, null, null, sortOrder);
            default:
                throw new UnsupportedOperationException("Uri not found:" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = URI_MATCHER.match(uri);
        switch (match){
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
            case MOVIE_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("uri not found:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase database = movieDataBaseHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIE: {
                long id = database.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = MovieContract.MovieEntry.buildMovieUri(id);
                else
                    throw new android.database.SQLException("failed to insert row into" + uri);
                break;
            }
        default:
            throw new UnsupportedOperationException("Uri not found:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = movieDataBaseHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);

        int rowDeleted;
        if ( null == selection)
            selection = "1";
        switch (match){
            case MOVIE:
                rowDeleted = database.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_ID:
                rowDeleted = database.delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", selectionArgs);
                break;
            default:
                throw  new UnsupportedOperationException("Uri not found:" + uri);
        }
        if (rowDeleted !=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = movieDataBaseHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int rowsUpdated;
        if (values == null){
            throw new IllegalArgumentException("content values not found");
        }
        switch (match){
            case MOVIE:
                rowsUpdated = database.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MOVIE_ID:
                rowsUpdated = database.update(MovieContract.MovieEntry.TABLE_NAME, values, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "= ?" , selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Uri not found:" + uri);
        }
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
