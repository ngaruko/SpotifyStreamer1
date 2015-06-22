package net.kiwigeeks.spotify;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by itl on 11/06/2015.
 */
public class TrackListData implements Parcelable {


    String trackName;
    String albumName;
    String largeAlbumThumbnail;
    String smallAlbumThumbnail;
    String previewUrl;
    int trackId;
     String duration;
    String artistName;
     String countryCode;

    //Constructors for TrackListData
    public TrackListData() {
    }

    public TrackListData(String trackName, String albumName, String artistName, String largeAlbumThumbnail, String smallAlbumThumbnail, String previewUrl, int trackId, String duration, String countryCode) {
        this.trackName = trackName;
        this.albumName = albumName;
        this.largeAlbumThumbnail = largeAlbumThumbnail;
        this.smallAlbumThumbnail = smallAlbumThumbnail;
        this.previewUrl = previewUrl;
        this.trackId = trackId;
        this.duration = duration;
        this.artistName = artistName;
        this.countryCode=countryCode;
    }


    private TrackListData(Parcel in) {
        artistName = in.readString();
        trackName = in.readString();
        albumName = in.readString();
        largeAlbumThumbnail = in.readString();
        smallAlbumThumbnail = in.readString();
        previewUrl = in.readString();
        trackId = in.readInt();
        duration = in.readString();
        countryCode=in.readString();
    }


    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel out, int flags) {

        out.writeString(artistName);
        out.writeString(trackName);
        out.writeString(albumName);
        out.writeString(largeAlbumThumbnail);
        out.writeString(smallAlbumThumbnail);
        out.writeString(previewUrl);
        out.writeInt(trackId);
        out.writeString(duration);
        out.writeString(countryCode);
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

    public String getDuration() {
        return duration;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public  String getArtistName(){return artistName;}

    public  void setArtistName(String artistName){this.artistName=artistName;}

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }


//endregion


}
