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
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
public class SelectedTrackActivityFragment extends Fragment {

    public SelectedTrackActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_selected_track, container, false);


        //TODO: Refactor: Instead of a separate layout for this, why not display the play the track straight away?
        final Intent intent = getActivity().getIntent();
        if (intent != null) {


            Bundle stringExtras = intent.getExtras();
            String trackName = stringExtras.getString("EXTRA_NAME");
            String album = stringExtras.getString("EXTRA_ALBUM");


            TextView textView = ((TextView) rootView.findViewById(R.id.selected_track_name));
            textView.setText(trackName);


            TextView textView2 = ((TextView) rootView.findViewById(R.id.selected_track_album));
            textView2.setText(album);

            ImageView image = (ImageView) rootView.findViewById(R.id.selected_track_thumbnail);


            Bundle extras = intent.getExtras();

            Bitmap bmp = (Bitmap) extras.getParcelable("imagebitmap");

            image.setImageBitmap(bmp);

        }//end if


        //Intent for items on the list
        rootView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                //TODO: Launch the Player in Stage 2
                Toast.makeText(getActivity(), "This will play your song...", Toast.LENGTH_LONG).show();

            }
        });


        return rootView;
    }


}
