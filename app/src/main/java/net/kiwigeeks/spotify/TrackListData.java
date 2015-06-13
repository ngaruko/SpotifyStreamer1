package net.kiwigeeks.spotify;

/**
 * Created by itl on 11/06/2015.
 */
public class TrackListData {


    String trackName;
    String albumName;
    String largeAlbumThumbnail;
    String smallAlbumThumbnail;
    String previewUrl;
    int trackId;

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

}
