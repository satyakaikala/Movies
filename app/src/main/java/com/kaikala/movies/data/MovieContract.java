package com.kaikala.movies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by skai0001 on 10/8/16.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.kaikala.movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String MOVIE_PATH = "movie";

    public static final class MovieEntry implements BaseColumns {
        //content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(MOVIE_PATH).build();
        //creating cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +CONTENT_AUTHORITY + "/" + MOVIE_PATH;
        //creating cursor of base type item for single entries
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +CONTENT_AUTHORITY + "/" + MOVIE_PATH;
        //table name
        public static final String TABLE_NAME = "movies";
        //columns
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";


        public static final String[] MOVIE_COLUMNS = {
                COLUMN_MOVIE_ID,
                COLUMN_MOVIE_TITLE,
                COLUMN_MOVIE_RELEASE_DATE,
                COLUMN_MOVIE_OVERVIEW,
                COLUMN_MOVIE_POSTER_PATH,
                COLUMN_MOVIE_VOTE_AVERAGE,
        };

        public static final int COL_MOVIE_ID = 0;
        public static final int COL_MOVIE_TITLE = 1;
        public static final int COL_MOVIE_RELEASE_DATE = 2;
        public static final int COL_MOVIE_OVERVIEW = 3;
        public static final int COL_MOVIE_POSTER_PATH = 4;
        public static final int COL_MOVIE_VOTE_AVERAGE = 5;

        // for building Uri's pn insertion
        public static  Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri movieUriId(String id){
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
    }
}
