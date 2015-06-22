package net.kiwigeeks.spotify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import net.kiwigeeks.spotify.ArtistsFragment.OnItemSelectedListener;


public class MainActivity extends ActionBarActivity implements OnItemSelectedListener {


    public boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        //Ask user to insert a query

        // String title = "Enter artist name!";
        String message = "Please enter the name of the artist in the top bar." +
                "\n Go to Settings to select the country.\n Press the arrow or the Search Key when done!";
        // notifyUser(title, message); //or maybe use a toast
        //Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();


        // setHasOptionsMenu(true);
        determinePaneLayout();
    }

    private void determinePaneLayout() {
        FrameLayout fragmentItemDetail = (FrameLayout) findViewById(R.id.top_track_container);
        if (fragmentItemDetail != null) {
            mTwoPane = true;
            ArtistsFragment fragmentItemsList =
                    (ArtistsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_artists);
            fragmentItemsList.setActivateOnItemClick(true);
        }
    }


    @Override
    public void onItemSelected(ArtistListData data) {
        if (mTwoPane) { // single activity with list and detail
            // Replace frame layout with correct detail fragment

            TrackActivityFragment fragmentTrack = TrackActivityFragment.newInstance(data);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.top_track_container, fragmentTrack);
            ft.commit();


        } else { // separate activities
            // launch detail activity using intent
            Intent intent = new Intent(this, TrackActivity.class);
            intent.putExtra("EXTRA_SPOTIFY_ID", data.getSpotifyId());
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}