package net.kiwigeeks.spotify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;

//import android.widget.SearchView;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistsFragment extends Fragment {


    //region members


    ArrayList<ArtistListData> ArtistList = new ArrayList<>();
    ArtistListData listData = new ArtistListData();
    private ArtistAdapter mArtistAdapter;
    private String searchQuery;
    private View rootView;
    private View progressbarView;

    //endregion

    public ArtistsFragment() {


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //Ask user to insert a query

        String title = "Enter artist name!";
        String message = "Please enter the name of the artist in the top bar." +
                "\n Go to Settings to select the country.\n Press the arrow or the Search Key when done!";
        notifyUser(title, message); //or maybe use a toast

    }



    @Override
    public void onStart() {
        super.onStart();

         progressbarView = rootView.findViewById(R.id.progress_bar_layout);
        progressbarView.setVisibility(View.GONE);


    }

    private void notifyUser(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_artist_fragment, menu);

        // Configure the search info and add any event listeners

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        handleSearchView(searchView);

    }

    private void handleSearchView(final SearchView search) {
        search.setQueryHint("Search Artists");
        search.setSubmitButtonEnabled(true);
        //*** setOnQueryTextFocusChangeListener ***
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO To add more code in Phase 2

            }
        });

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                listData.setSearchQuery(query);
                search.clearFocus();
                //display the Progress bar
                progressbarView.setVisibility(View.VISIBLE);

                searchForArtists();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO To do more tweaks in phase two or later

                return false;
            }


        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }


    //see if we can get data with spotify

    public void searchForArtists() {

        GetArtistDataTask artistDataTask = new GetArtistDataTask();
//validation here; ask user to enter the artist name

        searchQuery = listData.getSearchQuery();

        if (searchQuery != null) artistDataTask.execute(searchQuery);
        else
            Toast.makeText(getActivity(), "Please enter a valid, non-empty search query!! ", Toast.LENGTH_LONG).show();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_main, container, false);


        ListView artistsListView = (ListView) rootView.findViewById(R.id.listview_artists);

        mArtistAdapter = new ArtistAdapter(this.getActivity(), ArtistList);

        artistsListView.setAdapter(mArtistAdapter);

        artistsListView.setClickable(true);


        //Intent for items on the listView

        artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                ArtistAdapter adapter = (ArtistAdapter) adapterView.getAdapter();
                LinearLayout currentView = (LinearLayout) view;

                ArtistListData selectedArtist = adapter.getItem(position);

                String selectedArtistName = selectedArtist.getArtistName();
                listData.setArtistName(selectedArtistName);

                String spotifyId = selectedArtist.getSpotifyId();


                ImageView selectedArtistImageView = (ImageView) currentView.findViewById(R.id.artist_thumbnail);


                selectedArtistImageView.buildDrawingCache();
                Bitmap image = selectedArtistImageView.getDrawingCache();

                Bundle extras = new Bundle();
                extras.putParcelable("imagebitmap", image);


                Intent intent = new Intent(getActivity(), SelectedArtist.class)
                        .putExtra("EXTRA_ARTIST_NAME", selectedArtistName)
                        .putExtra("EXTRA_SPOTIFY_ID", spotifyId)

                        .putExtras(extras);

                startActivity(intent);
            }
        });

        //END ONCLICK LISTNER
        return rootView;
    }


    //Async Task to get data from Spotify
    public class GetArtistDataTask extends AsyncTask<String, Void, List<Artist>> {


        @Override
        protected List<Artist> doInBackground(String... params) {

            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();


                ArtistsPager results = spotify.searchArtists(searchQuery);
                return  results.artists.items;

            } catch (RetrofitError error) {

                // handle error
                error.printStackTrace();
            } finally {
                Log.e("Bad connection", "closing now");


            }
            //return new ;
            return null;
        }


        @Override
        protected void onPostExecute(List<Artist> artists) {


            if (artists == null)

                notifyUser("Connection Error", "Please check your internet connections and try again!");


            else if (artists.isEmpty())
                Toast.makeText(getActivity(), "NO ARTIST FOUND.PLEASE TRY ANOTHER SEARCH! ", Toast.LENGTH_LONG).show();
            else {


                ArtistList.clear();

                for (Artist artist : artists) {

                    ArtistListData myArtist = new ArtistListData();


                    try {

                        myArtist.setArtistName(artist.name);

                        myArtist.setSpotifyId(artist.id);
                        myArtist.setCountryCode("US");
                        myArtist.setArtistImageUrl(artist.images.get(0).url);

                        Log.e("Artist object: ", ArtistList.get(0).getArtistName());
                        Log.e("Name: ", artist.name);
                        Log.e("IMAGE: ", artist.images.get(0).url);


                    } catch (IndexOutOfBoundsException e) {
                        Log.e("Error ", e.toString());

                    } finally {
                        Log.e("Error", "No picture");

                        //add a placeholder image or one can add a null and let Picasso use the "error" image if provided.
                        // !Please don't send an empty string!

                        if (artist.images.isEmpty())
                            myArtist.setArtistImageUrl("http://greentreesarborcareinc.com/wp-content/uploads/2014/01/image-placeholder.jpg");


                    }//END finally

                    ArtistList.add(myArtist);

                }//end for
                mArtistAdapter.notifyDataSetChanged();

            }


        }

    }
}




