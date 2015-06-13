package net.kiwigeeks.spotify;



/*
 * ArtistListData class will hold data for displaying in ListView and use elsewhere in tha app
 * */
public class ArtistListData {

    String artistImageUrl;
    String artistName;
    String spotifyId;
    int artistId;
    String countryCode;

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


}
