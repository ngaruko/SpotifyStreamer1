package net.kiwigeeks.spotify.data;

//import android.content.Context;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.kiwigeeks.spotify.TrackListData;
import net.kiwigeeks.spotify.data.TrackContract.TrackEntry;

import java.util.ArrayList;



public class TrackDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 6;

    static final String DATABASE_NAME = "track.db";

    public TrackDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TRACK_TABLE = "CREATE TABLE " + TrackEntry.TABLE_NAME + " (" +


                TrackEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +


                TrackEntry.COLUMN_ARTIST_NAME + " TEXT NOT NULL, " +
                TrackEntry.COLUMN_TRACK_NAME + " TEXT NOT NULL, " +
                TrackEntry.COLUMN_ALBUM_NAME + " TEXT NOT NULL, " +
                TrackEntry.COLUMN_LARGE_ALBUM_ARTWORK + " TEXT NOT NULL, " +
                TrackEntry.COLUMN_SMALL_ALBUM_ARTWORK + " TEXT NOT NULL, " +
                TrackEntry.COLUMN_PREVIEW_URL + " TEXT NOT NULL, " +
                TrackEntry.COLUMN_COUNTRY_CODE + " TEXT NOT NULL, " +


                TrackEntry.COLUMN_TRACK_DURATION + " INTEGER NOT NULL " +

                " )";

        db.execSQL(SQL_CREATE_TRACK_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TrackEntry.TABLE_NAME);
        onCreate(db);

    }


    // Adding new track
    public void addTracks(TrackListData track) {
        SQLiteDatabase db = this.getWritableDatabase();


        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TrackEntry.COLUMN_TRACK_NAME, track.getTrackName());
        values.put(TrackEntry.COLUMN_ALBUM_NAME, track.getAlbumName());
        values.put(TrackEntry.COLUMN_LARGE_ALBUM_ARTWORK, track.getLargeAlbumThumbnail());
        values.put(TrackEntry.COLUMN_SMALL_ALBUM_ARTWORK, track.getSmallAlbumThumbnail());
        values.put(TrackEntry.COLUMN_PREVIEW_URL, track.getPreviewUrl());
        values.put(TrackContract.TrackEntry.COLUMN_COUNTRY_CODE, track.getCountryCode());
        values.put(TrackEntry.COLUMN_TRACK_DURATION, track.getDuration());
        values.put(TrackEntry.COLUMN_ARTIST_NAME, track.getArtistName());

        // Inserting Row
        db.insert("tracks", null, values);
        db.close(); // Closing database connection
    }


    public ArrayList<TrackListData> getAllTracks() {
        //return dataList here...
        ArrayList<TrackListData> playList = new ArrayList<>();
// Select All Query
        String selectQuery = "SELECT  * FROM " + "tracks";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TrackListData track = new TrackListData();

                track.setArtistName(cursor.getString(cursor.getColumnIndex(TrackEntry.COLUMN_ARTIST_NAME)));
                track.setTrackName(cursor.getString(cursor.getColumnIndex(TrackEntry.COLUMN_TRACK_NAME)));
                track.setAlbumName(cursor.getString(cursor.getColumnIndex(TrackEntry.COLUMN_ALBUM_NAME)));
                track.setLargeAlbumThumbnail(cursor.getString(cursor.getColumnIndex(TrackEntry.COLUMN_LARGE_ALBUM_ARTWORK)));
                track.setSmallAlbumThumbnail(cursor.getString(cursor.getColumnIndex(TrackEntry.COLUMN_SMALL_ALBUM_ARTWORK)));
                track.setPreviewUrl(cursor.getString(cursor.getColumnIndex(TrackEntry.COLUMN_PREVIEW_URL)));
                track.setCountryCode(cursor.getString(cursor.getColumnIndex(TrackEntry.COLUMN_COUNTRY_CODE)));
                track.setDuration(cursor.getString(cursor.getColumnIndex(TrackEntry.COLUMN_TRACK_DURATION)));

                // Adding track to list
                playList.add(track);
            } while (cursor.moveToNext());
        }

        // return contact list


        return playList;
    }

    public void resetDb (){


        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM tracks"); //delete all rows in a table

    //  db.close();
     // this.onCreate(db);


    }
}
