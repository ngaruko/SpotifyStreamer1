package net.kiwigeeks.spotify;


import android.os.Parcel;
import android.os.Parcelable;

/*
 * ArtistListData class will hold data for displaying in ListView and use elsewhere in tha app
 * */
public class ArtistListData implements Parcelable {

    String artistImageUrl;
    String artistName;
    String spotifyId;
    int artistId;
    String countryCode;

    public ArtistListData(String searchQuery, String artistImageUrl, String artistName, String spotifyId, int artistId, String countryCode) {
        this.searchQuery = searchQuery;
        this.artistImageUrl = artistImageUrl;
        this.artistName = artistName;
        this.spotifyId = spotifyId;
        this.artistId = artistId;
        this.countryCode = countryCode;
    }

    public ArtistListData() {
    }


    private ArtistListData(Parcel in){
        searchQuery=in.readString();
        artistName=in.readString();
        artistImageUrl=in.readString();
        spotifyId=in.readString();
        countryCode=in.readString();
        artistId=in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {

        out.writeString(searchQuery);
        out.writeString(artistName);
        out.writeString(artistImageUrl);
        out.writeString(spotifyId);
        out.writeString(countryCode);
        out.writeInt(artistId);
    }
    public static final Parcelable.Creator<ArtistListData> CREATOR
            = new Parcelable.Creator<ArtistListData>() {
        public ArtistListData createFromParcel(Parcel in) {
            return new ArtistListData(in);
        }

        @Override
        public ArtistListData[] newArray(int size) {
            return new ArtistListData[size];
        }
    };


    //region Getters and setters

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    String searchQuery;


    //Setters and Getters
    public String getArtistImageUrl() {
        return artistImageUrl;
    }

    public void setArtistImageUrl(String artistImageUrl) {
        this.artistImageUrl = artistImageUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }


    public String getSpotifyId() {
        return spotifyId;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getArtistId() {
        return artistId;
    }


    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }


//endregion

}
