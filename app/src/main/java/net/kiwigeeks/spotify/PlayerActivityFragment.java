package net.kiwigeeks.spotify;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
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

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerActivityFragment extends DialogFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    //public  ;

    private static final String LOG_TAG = "Connection result: ";
    public MusicService mService;

    Intent playIntent;
    boolean mBound = false;


    private ImageButton previousButton, playPauseButton, nextButton;
    //private boolean isPlaying = true;
    // static MediaPlayer mediaPlayer;
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



    public PlayerActivityFragment() {
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance

            Log.d(LOG_TAG, "onServiceConnected now");
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.setPlayList(playList);
            // mService.setPosition(trackIndex);

            //playTrack(trackIndex);
            populateControllerUI(trackIndex);
            mService.initMusicPlayer(trackIndex);

            Log.d("Connected", "happy");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            Log.d(LOG_TAG, "onServiceDisconnected");
            mBound = false;
        }
    };


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        Log.e("START", "OnAttach!");
        // Bind to LocalService


        // Bind to LocalService
        if (playIntent == null) {
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, mConnection, Context.BIND_AUTO_CREATE);
        }

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Activity:...", String.valueOf(this.getActivity()));

       // setRetainInstance(true);

        if (savedInstanceState != null && savedInstanceState.containsKey("playList")) {

            playList = savedInstanceState.getParcelableArrayList("playList");
            trackIndex=savedInstanceState.getInt("trackIndex");

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onStart() {
        super.onStart();
        // Log.e("Onstart", mService.getPlayList().get(1).getArtistName());

    }


    @Override
    public void onResume() {
        super.onResume();
        // Log.e("Resume", mService.getPlayList().get(1).getArtistName());

    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
//        if (mBound) {
//            getActivity().unbindService(mConnection);
//            mBound = false;
//        }
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

        //playPauseButton.setImageResource(android.R.drawable.ic_media_play);
        Log.d("onpause", "posing");
        //mService.pause();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_player, container, false);

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
        loadingLayout.setVisibility(View.GONE);


        utils = new Utilities();
        // Listeners
        trackProgressBar.setOnSeekBarChangeListener(this);
        Log.e("Stage: ", "onCreateView");

        Log.e("Playing: ", "I will play");

        return rootView;

    }

    private void playTrack(int index) {
//        mService.playSong(index);
        //Populate other fields


        //mService.setPlayList(new TrackDbHelper(getActivity()).getAllTracks());

        Log.d("some data from service", mService.getPlayList().get(3).getTrackName());

        //  mService.setPosition(index);
        mService.initMusicPlayer(index);
        populateControllerUI(index);
        updateProgressBar();
    }

    private void populateControllerUI(int index) {
        TextView textView = ((TextView) rootView.findViewById(R.id.selected_artist_name));

        textView.setText(playList.get(index).getArtistName());

        TextView trackTextView = ((TextView) rootView.findViewById(R.id.selected_track_name));
        trackTextView.setText(playList.get(index).getTrackName());


        TextView textView2 = ((TextView) rootView.findViewById(R.id.selected_track_album));
        textView2.setText(playList.get(index).getAlbumName());

        ImageView image = (ImageView) rootView.findViewById(R.id.selected_track_thumbnail);


        Picasso.with(getActivity()).load(playList.get(index).getLargeAlbumThumbnail()).resize(400, 400)

                .into(image);
        // Changing Button Image to pause image
        //if (mService.isPlaying())
        playPauseButton.setImageResource(android.R.drawable.ic_media_pause);

        //else playPauseButton.setImageResource(android.R.drawable.ic_media_play);
        // set Progress bar values
        trackProgressBar.setProgress(0);
        trackProgressBar.setMax(100);
        updateProgressBar();


      //  if (mService.isCompleted) playPauseButton.setImageResource(android.R.drawable.ic_media_play);

        // Updating progress bar



        loadingLayout.setVisibility(View.GONE);
    }


    // ItemDetailFragment.newInstance(item)
    public static PlayerActivityFragment newInstance(int index)

    {
        PlayerActivityFragment playFragment = new PlayerActivityFragment();

        Bundle args = new Bundle();
        args.putInt("trackIndex", index);
        playFragment.setArguments(args);
        return playFragment;
    }


//Control media

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.previous_button:
                //playPrevious track;
                if (mService.isPlaying()) mService.pause();

                if (trackIndex > 0) {
                    playTrack(trackIndex - 1);
                    trackIndex = trackIndex - 1;
                } else {
                    // play last song
                    playTrack(playList.size() - 1);
                    trackIndex = playList.size() - 1;
                }
                break;

            case R.id.next_button:
                //playPrevious track;
                if (mService.isPlaying()) mService.pause();
                // check if next song is there or not
                if (trackIndex < (playList.size() - 1)) {
                    playTrack(trackIndex + 1);
                    trackIndex = trackIndex + 1;
                } else {
                    // play first song
                    playTrack(0);
                    trackIndex = 0;
                }
                break;

            case R.id.play_pause_button:
                if (mService.isPlaying()) {

                    mService.pause();
                    playPauseButton.setImageResource(android.R.drawable.ic_media_play);

                } else {
                    //resume song
                    mService.restart();

                    playPauseButton.setImageResource(android.R.drawable.ic_media_pause);

                }

                break;

        }

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

            long currentDuration = mService.getCurrentPosition();

            // Displaying Total Duration time
            trackTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));

            // Displaying time completed playing
            trackCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            trackProgressBar.setProgress(progress);
            if (mService.isPlaying()) playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
else   playPauseButton.setImageResource(android.R.drawable.ic_media_play);
            //if (mService.isCompleted) playPauseButton.setImageResource(android.R.drawable.ic_media_play);

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
        //int totalDuration = mService.getDuration();
       // int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
      //  mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }



    //endregion





}
