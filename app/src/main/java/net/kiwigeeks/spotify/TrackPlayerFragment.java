package net.kiwigeeks.spotify;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.kiwigeeks.spotify.data.TrackDbHelper;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class TrackPlayerFragment extends DialogFragment implements View.OnClickListener,
        MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private ImageButton previousButton, playPauseButton, nextButton;
    //private boolean isPlaying = true;
    static MediaPlayer mediaPlayer ;
    private SeekBar trackProgressBar;
    private TextView trackCurrentDurationLabel;
    public TextView trackTotalDurationLabel;


    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();

    private Utilities utils;
    private int trackIndex;
    private ArrayList<TrackListData> playList;
    private View rootView;
    private LinearLayout loadingLayout;

    public TrackPlayerFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (savedInstanceState != null && savedInstanceState.containsKey("playList")) {

            playList = savedInstanceState.getParcelableArrayList("playList");

        } else {
            TrackDbHelper myDbHelper = new TrackDbHelper(getActivity());
            playList = myDbHelper.getAllTracks();
        }


        final Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra("EXTRA_TRACK_INDEX")) {

            trackIndex = intent.getIntExtra("EXTRA_TRACK_INDEX", 0);

        } else trackIndex = getArguments().getInt("trackIndex");


    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save current data

        savedInstanceState.putParcelableArrayList("playList", playList);
        savedInstanceState.putInt("trackIndex", trackIndex);


    }

    @Override
    public void onPause() {
        super.onPause();

        playPauseButton.setImageResource(android.R.drawable.ic_media_play);
    }

    @Override
    public void onDestroyView()
    {
        Dialog dialog = getDialog();

        // Work around bug: http://code.google.com/p/android/issues/detail?id=17423
        if ((dialog != null) && getRetainInstance())
            dialog.setDismissMessage(null);

        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_selected_track, container, false);

        //Get the control buttons
        previousButton = (ImageButton) rootView.findViewById(R.id.previous_button);
        previousButton.setOnClickListener(this); // calling onClick() method
        playPauseButton = (ImageButton) rootView.findViewById(R.id.play_pause_button);
        playPauseButton.setOnClickListener(this);
        nextButton = (ImageButton) rootView.findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);
        trackProgressBar = (SeekBar) rootView.findViewById(R.id.track_progressbar);

        trackTotalDurationLabel = (TextView) rootView.findViewById(R.id.trackTotalDurationLabel);
        trackCurrentDurationLabel = (TextView) rootView.findViewById(R.id.trackCurrentDurationLabel);

        loadingLayout = (LinearLayout) rootView.findViewById(R.id.loading);
        if (mediaPlayer!=null && mediaPlayer.isPlaying()) loadingLayout.setVisibility(View.GONE);


        utils = new Utilities();
        // Listeners
        trackProgressBar.setOnSeekBarChangeListener(this);


      // if (mediaPlayer!=null && !mediaPlayer.isPlaying()) {
           try {
               playTrack(trackIndex);


               //Control media Player


           } catch (IOException e) {
               e.printStackTrace();
           }

           return rootView;

    }

    // ItemDetailFragment.newInstance(item)
    public static TrackPlayerFragment newInstance(int index)

    {
        TrackPlayerFragment playFragment = new TrackPlayerFragment();

        Bundle args = new Bundle();
        args.putInt("trackIndex", index);
        playFragment.setArguments(args);
        return playFragment;
    }


    private void playTrack(int trackIndex) throws IOException {




        //Populate other fields

        TextView textView = ((TextView) rootView.findViewById(R.id.selected_artist_name));

        textView.setText(playList.get(trackIndex).getArtistName());

        TextView trackTextView = ((TextView) rootView.findViewById(R.id.selected_track_name));
        trackTextView.setText(playList.get(trackIndex).getTrackName());


        TextView textView2 = ((TextView) rootView.findViewById(R.id.selected_track_album));
        textView2.setText(playList.get(trackIndex).getAlbumName());

        ImageView image = (ImageView) rootView.findViewById(R.id.selected_track_thumbnail);


        Picasso.with(getActivity()).load(playList.get(trackIndex).getLargeAlbumThumbnail()).resize(400, 400)

                .into(image);
        // Changing Button Image to pause image
        playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
        // set Progress bar values
        trackProgressBar.setProgress(0);
        trackProgressBar.setMax(100);

        // Updating progress bar
        updateProgressBar();


        //MediaPlayer

       if (mediaPlayer == null)      mediaPlayer = new MediaPlayer();

     if(!mediaPlayer.isPlaying()) {
         try {
             mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
             mediaPlayer.reset();
             // MediaPlayer mediaPlayer = MediaPlayer.create(context, URI.parse("file://" + filePath));
             mediaPlayer.setDataSource(playList.get(trackIndex).getPreviewUrl());
             mediaPlayer.setOnPreparedListener(this);
             mediaPlayer.setOnErrorListener(this);
             mediaPlayer.setOnCompletionListener(this);
             mediaPlayer.prepareAsync();
             loadingLayout.setVisibility(View.VISIBLE);

         } catch (IllegalArgumentException | IllegalStateException | IOException e) {
             e.printStackTrace();
         }
     }

    }


    //Control media

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.previous_button:
                //playPrevious track;

                if (trackIndex > 0) {
                    try {
                        playTrack(trackIndex - 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    trackIndex = trackIndex - 1;
                } else {
                    // play last song
                    try {
                        playTrack(playList.size() - 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    trackIndex = playList.size() - 1;
                }
                break;

            case R.id.next_button:
                //playPrevious track;

                // check if next song is there or not
                if (trackIndex < (playList.size() - 1)) {
                    try {
                        playTrack(trackIndex + 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    trackIndex = trackIndex + 1;
                } else {
                    // play first song
                    try {
                        playTrack(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    trackIndex = 0;
                }
                break;

            case R.id.play_pause_button:
                if (mediaPlayer.isPlaying()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        playPauseButton.setImageResource(android.R.drawable.ic_media_play);
                    }

                } else {
                    //resume song
                    if (mediaPlayer != null) {

                        mediaPlayer.start();
                        playPauseButton.setImageResource(android.R.drawable.ic_media_pause);

                    }
                }

                break;

        }

    }


    @Override
    public void onCompletion(MediaPlayer mp) {

        playPauseButton.setImageResource(android.R.drawable.ic_media_play);

    }


    //Region Seekbar and Timer

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            long totalDuration = Long.parseLong(playList.get(trackIndex).getDuration());

            long currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            trackTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));

            // Displaying time completed playing
            trackCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            trackProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        loadingLayout.setVisibility(View.GONE);

        mp.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


    //endregion

}
