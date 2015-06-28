package net.kiwigeeks.spotify;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.kiwigeeks.spotify.data.TrackDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

//import net.kiwigeeks.spotify.data.TrackContract.TrackEntry;


/**
 * A placeholder fragment containing a simple view.
 */
public class TrackActivityFragment extends Fragment {

    public  TrackActivityFragment() {}
  public   ArrayList<TrackListData> TrackList = new ArrayList<>();


    TracksAdapter mTracksAdapter;
    String countryCode;
    public static String spotifyId;



    //set listener




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        countryCode = pref
                .getString(getString(R.string.pref_country_key),
                        getString(R.string.pref_country_default));

        //  countryCode="US";


        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("EXTRA_SPOTIFY_ID")) {

            spotifyId = intent.getStringExtra("EXTRA_SPOTIFY_ID");

        } else spotifyId = getArguments().getString("spotifyId");
        //redo this code

        //if (spotifyId==null) spotifyId=getArguments().getString("spotifyId");


        if (savedInstanceState == null || !savedInstanceState.containsKey("tracklist"))
            updateTracks();


        else {
            TrackList = savedInstanceState.getParcelableArrayList("tracklist");
        }


    }




    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save current data


        savedInstanceState.putParcelableArrayList("tracklist", TrackList);


    }

    protected void updateTracks() {
        GetTracksDataTask tracksDataTask = new GetTracksDataTask();


        tracksDataTask.execute(countryCode);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);


        View rootView = inflater.inflate(R.layout.fragment_track, container, false);

        final ListView trackListView = (ListView) rootView.findViewById(R.id.listview_tracks);


        //check out this code
        mTracksAdapter = new TracksAdapter(this.getActivity(), TrackList);


        trackListView.setAdapter(mTracksAdapter);

        trackListView.setClickable(true);


        //Intent when list clicked


        trackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TracksAdapter adapter = (TracksAdapter) adapterView.getAdapter();


                TrackListData selectedTrack = adapter.getItem(position);


                MainActivity ma = new MainActivity();
                if (ma.mTwoPane = true) {
                    //Todo something

                    //  DialogFragment newFragment =  TrackPlayerFragment.newInstance(position);

                    DialogFragment newFragment = PlayerActivityFragment.newInstance(position);
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();

                    newFragment.show(ft, "");
//


                } else {


                    Intent intent = new Intent(getActivity(), PlayerActivity.class);

                    intent.putExtra("EXTRA_TRACK_INDEX", position);

                    startActivity(intent);

                }




            }
        });


        return rootView;





    }

    // ItemDetailFragment.newInstance(item)
    public static TrackActivityFragment newInstance(ArtistListData data)

    {
        TrackActivityFragment trackFragment = new TrackActivityFragment();

        Bundle args = new Bundle();
        args.putString("spotifyId", data.getSpotifyId());
        trackFragment.setArguments(args);
        return trackFragment;
    }


    //Async Task to get data from Spotify
    public class GetTracksDataTask extends AsyncTask<String, Void, List<Track>> {


        //private List<Track> mitracks=new ArrayList<>();
        private TrackListData myTrack;
        private Tracks topTracks;


        @Override
        protected List<Track> doInBackground(String... params) {

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            HashMap<String, Object> options = new HashMap<>();
            options.put("country", countryCode);

            Log.e("Country code...", countryCode);
            Log.e("spotify id...", spotifyId);

            try {
                topTracks = spotify.getArtistTopTrack(spotifyId, options);
                return topTracks.tracks;
            } catch (RetrofitError error) {
                SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
                Log.e("Spotify Error: ", spotifyError.getMessage());


            }


            return null;
        }


        @Override
        protected void onPostExecute(List<Track> tracks) {

        //Clear database here
            new TrackDbHelper(getActivity()).resetDb();


            if (tracks == null || tracks.isEmpty()) {
                Log.e("Error", "Nothing received!");
                Toast.makeText(getActivity(), "NO TRACKS FOUND. PLEASE TRY AGAIN LATER", Toast.LENGTH_LONG).show();
            } else {
                TrackList.clear();


                for (Track track : tracks) {

                    myTrack = new TrackListData();


                    try {
                        //set values for the data


                        myTrack.setTrackName(track.name);
                        myTrack.setAlbumName(track.album.name);
                        myTrack.setPreviewUrl(track.preview_url);
                        myTrack.setDuration(Long.toString(track.duration_ms));
                        myTrack.setArtistName(track.artists.get(0).name);
                        myTrack.setCountryCode(countryCode);


                        List<Image> image = track.album.images;

                        if (image != null && !image.isEmpty()) {
                            myTrack.setLargeAlbumThumbnail(image.get(0).url);

                            myTrack.setSmallAlbumThumbnail(image.get(image.size() - 1).url);

                        }


                    } catch (IndexOutOfBoundsException e) {
                        Log.e("Error ", e.toString());

                    } finally {
                        Log.e("Error ", "error");
                        //placeholders for images
                        if (track.album.images == null || track.album.images.isEmpty()) {
                            myTrack.setLargeAlbumThumbnail("http://greentreesarborcareinc.com/wp-content/uploads/2014/01/image-placeholder.jpg");
                            myTrack.setSmallAlbumThumbnail("http://greentreesarborcareinc.com/wp-content/uploads/2014/01/image-placeholder.jpg");
                        }


                    }//end finally

                  //  TrackList.add(myTrack);
                    Log.e("Track", myTrack.getTrackName());
                    Log.e("Album", myTrack.getAlbumName());
                    Log.e("Thumb", myTrack.getPreviewUrl());
                    Log.e("Artist to show", myTrack.getArtistName());




                    insertDataToDatabase(myTrack);




                }//end for


                populateTrackListFromDb();

                mTracksAdapter.notifyDataSetChanged();

            }


        }

        private void insertDataToDatabase(TrackListData myTrack) {

            TrackDbHelper mDbHelper = new TrackDbHelper(getActivity());


            mDbHelper.addTracks(myTrack);
        }


        private void populateTrackListFromDb() {
            TrackDbHelper mDbHelper = new TrackDbHelper(getActivity());

            // Gets the data repository in write mode


            TrackList.addAll(mDbHelper.getAllTracks());




            Log.e("Element 1...", TrackList.get(0).getTrackName());
            Log.e("Element 2...", TrackList.get(1).getTrackName());

            for(TrackListData t: TrackList) {
                Log.e("Name..",  t.getTrackName());
            }
        }

    }


}




