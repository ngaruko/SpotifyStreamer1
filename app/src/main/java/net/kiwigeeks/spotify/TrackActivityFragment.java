package net.kiwigeeks.spotify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TrackActivityFragment extends Fragment {


    ArrayList<TrackListData> TrackList = new ArrayList<>();


    //int[] img = new int[0];
    TrackListData trackData = new TrackListData();


    private TracksAdapter mTracksAdapter;
    private String countryCode;
    private String spotifyId;


    public TrackActivityFragment() {
    }


    @Override
    public void onStart() {
        super.onStart();


        Intent intent = getActivity().getIntent();
        if (intent != null) {
            spotifyId = intent.getStringExtra("EXTRA_SPOTIFY_ID");
            updateTracks();
        }


    }

    private void updateTracks() {
        GetTracksDataTask tracksDataTask = new GetTracksDataTask();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        countryCode = pref
                .getString(getString(R.string.pref_country_key),
                        getString(R.string.pref_country_default));

        tracksDataTask.execute(countryCode);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_track, container, false);

        ListView trackListView = (ListView) rootView.findViewById(R.id.listview_tracks);


        //check out this code
        mTracksAdapter = new TracksAdapter(this.getActivity(), TrackList);


        trackListView.setAdapter(mTracksAdapter);

        trackListView.setClickable(true);


        //Intent when list clicked


        trackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TracksAdapter adapter = (TracksAdapter) adapterView.getAdapter();
                LinearLayout currentView = (LinearLayout) view;

                TrackListData selectedTrack = adapter.getItem(position);

                String selectedTrackName = selectedTrack.getTrackName();
                trackData.setTrackName(selectedTrackName);

                String selectedAlbumName = selectedTrack.getAlbumName();
                trackData.setAlbumName(selectedAlbumName);


                ImageView selectedTrackImageView = (ImageView) currentView.findViewById(R.id.track_thumbnail);
                // Bitmap bitmap = BitmapFactory.decodeResource(selectedArtistImage);
//

                selectedTrackImageView.buildDrawingCache();
                Bitmap image = selectedTrackImageView.getDrawingCache();

                Bundle extras = new Bundle();
                extras.putParcelable("imagebitmap", image);

                Bundle stringExtras = new Bundle();
                stringExtras.putString("EXTRA_NAME", selectedTrackName);
                stringExtras.putString("EXTRA_ALBUM", selectedAlbumName);

                Intent intent = new Intent(getActivity(), SelectedTrackActivity.class);

                intent.putExtras(extras);
                intent.putExtras(stringExtras);

                startActivity(intent);
            }
        });
        return rootView;
    }


    //Async Task to get data from Spotify
    public class GetTracksDataTask extends AsyncTask<String, Void, List<Track>> {


        @Override
        protected List<Track> doInBackground(String... params) {

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            HashMap<String, Object> options = new HashMap<>();
            options.put("country", countryCode);
            Tracks toptTracks = spotify.getArtistTopTrack(spotifyId, options);

            return toptTracks.tracks;

        }


        @Override
        protected void onPostExecute(List<Track> topTrucks) {


            if (topTrucks == null || topTrucks.isEmpty())
                Toast.makeText(getActivity(), "NO TRACKS FOUND. PLEASE TRY AGAIN", Toast.LENGTH_LONG).show();


            else {
                TrackList.clear();

                for (Track track : topTrucks) {

                    TrackListData myTrack = new TrackListData();


                    try {
                            //set values for the data

                        myTrack.setTrackName(track.name);
                        myTrack.setAlbumName(track.album.name);
                        myTrack.setPreviewUrl(track.preview_url);

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

                    TrackList.add(myTrack);
                    Log.e("Track", myTrack.getTrackName());
                    Log.e("Album", myTrack.getAlbumName());
                    Log.e("Thumb", myTrack.getPreviewUrl());


                }
                Log.e("Numbe: ", Integer.toString(TrackList.size()));
                Log.e("From List", TrackList.get(0).getTrackName());
                Log.e("Country Code", countryCode);


                //Update adapter/listview
                mTracksAdapter.notifyDataSetChanged();


                //Save to file

                try {
                    saveToFile(topTrucks);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }


        }

        private void saveToFile(List<Track> topTrucks) throws FileNotFoundException {


            ArrayList<HashMap<String, String>> topTrackList = new ArrayList<>();

            for (Track track : topTrucks) {
                HashMap<String, String> trackMap = new HashMap<>();
                trackMap.put("track", track.name);
                trackMap.put("artist", track.artists.get(0).name);
                trackMap.put("album", track.album.name);

                topTrackList.add(trackMap);

            }


            for (int a = 0; a < topTrackList.size(); a++) {
                //serialise
                HashMap<String, String> tempData = topTrackList.get(a);
                Set<String> key = tempData.keySet();
                Iterator it = key.iterator();
                while (it.hasNext()) {
                    String hmKey = (String) it.next();
                    String hmData = tempData.get(hmKey);


                    //save now

                    String trackData = "Key: " + hmKey + " & Data: " + hmData;
                    String filename = "toptracks.txt";

                    File file = new File(getActivity().getFilesDir(), filename);

                    FileOutputStream stream = new FileOutputStream(file);
                    try {
                        stream.write(trackData.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    it.remove(); // avoids a ConcurrentModificationException
                }


            }

        }
    }
}