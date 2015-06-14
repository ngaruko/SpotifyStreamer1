package net.kiwigeeks.spotify;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by itl on 11/06/2015.
 */
public class TrackListData implements Parcelable{


    String trackName;
    String albumName;
    String largeAlbumThumbnail;
    String smallAlbumThumbnail;
    String previewUrl;
    int trackId;

    //Constructors for TrackListData
    public TrackListData() {
    }
    public TrackListData(String trackName, String albumName, String largeAlbumThumbnail, String smallAlbumThumbnail, String previewUrl, int trackId) {
        this.trackName = trackName;
        this.albumName = albumName;
        this.largeAlbumThumbnail = largeAlbumThumbnail;
        this.smallAlbumThumbnail = smallAlbumThumbnail;
        this.previewUrl = previewUrl;
        this.trackId = trackId;
    }



    private TrackListData(Parcel in){
        trackName=in.readString();
        albumName=in.readString();
        largeAlbumThumbnail=in.readString();
        smallAlbumThumbnail=in.readString();
        previewUrl=in.readString();
        trackId=in.readInt();
    }


    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel out, int flags) {

        out.writeString(trackName);
        out.writeString(albumName);
        out.writeString(largeAlbumThumbnail);
        out.writeString(smallAlbumThumbnail);
        out.writeString(previewUrl);
        out.writeInt(trackId);
    }
    public static final Parcelable.Creator<TrackListData> CREATOR
            = new Parcelable.Creator<TrackListData>() {
        public TrackListData createFromParcel(Parcel in) {
            return new TrackListData(in);
        }

        @Override
        public TrackListData[] newArray(int size) {
            return new TrackListData[size];
        }
    };


//region getters and setters
        //Some members not used in Stage one. Please don't clean!

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getLargeAlbumThumbnail() {
        return largeAlbumThumbnail;
    }

    public void setLargeAlbumThumbnail(String largeAlbumThumbnail) {
        this.largeAlbumThumbnail = largeAlbumThumbnail;
    }

    public String getSmallAlbumThumbnail() {
        return smallAlbumThumbnail;
    }

    public void setSmallAlbumThumbnail(String smallAlbumThumbnail) {
        this.smallAlbumThumbnail = smallAlbumThumbnail;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public int getTrackId() {
        return trackId;
    }

//endregion


}
