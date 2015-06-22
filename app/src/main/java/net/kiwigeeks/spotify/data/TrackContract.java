package net.kiwigeeks.spotify.data;

import android.provider.BaseColumns;

public class TrackContract {


    /* Inner class that defines the contents of the tracks table */
    public static final class TrackEntry implements BaseColumns {

        public static final String TABLE_NAME = "tracks";

        // Column with the foreign key into the country table.
      //  public static final String COLUMN_COUNTRY_KEY = "country_id";

        public static final String COLUMN_ARTIST_NAME = "artist_name";

        public static final String COLUMN_TRACK_NAME = "track_name";

        public static final String COLUMN_ALBUM_NAME   = "album_name";


        public static final String COLUMN_LARGE_ALBUM_ARTWORK = "large_album_artwork";
        public static final String COLUMN_SMALL_ALBUM_ARTWORK = "small_album_artwork";

        // Track duration (stored as long)
        public static final String COLUMN_TRACK_DURATION = "track_duration";


        // Preview Url
        public static final String COLUMN_PREVIEW_URL = "preview_url";

        //Country name and code for user selection (can be in different table?)
        public static final String COLUMN_COUNTRY_CODE = "country_code";
        //public static final String COLUMN_COUNTRY_NAME = "country_name";


    }


}
