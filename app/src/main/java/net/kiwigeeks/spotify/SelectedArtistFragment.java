package net.kiwigeeks.spotify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class SelectedArtistFragment extends Fragment {

    private String spotifyId;

    public SelectedArtistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_selected_artist, container, false);


        //The Selected Artist Activity called via Intent. Inspect the intent for  data.

        //TODO: Refactor: Instead of a separate layout for this. Why not display the top tracks straight away?


        final Intent intent = getActivity().getIntent();
        if (intent != null  ) {
            String artistName = intent.getStringExtra("EXTRA_ARTIST_NAME");
             spotifyId = intent.getStringExtra("EXTRA_SPOTIFY_ID");




            TextView textView = ((TextView) rootView.findViewById(R.id.selected_artist_name));
            textView.setText(artistName);

           ImageView  image = (ImageView)rootView.findViewById(R.id.selected_artist_thumbnail);


            Bundle extras = intent.getExtras();
            Bitmap bmp = (Bitmap) extras.getParcelable("imagebitmap");

            image.setImageBitmap(bmp );

        }//end if


            //Intent for items on the list
            rootView.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(getActivity(), TrackActivity.class);
                    intent.putExtra("EXTRA_SPOTIFY_ID", spotifyId);
                    startActivity(intent);

                }
            });






    return rootView;

    }
}
