package net.kiwigeeks.spotify;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service implements
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {


    //media mediaPlayer
    private MediaPlayer mediaPlayer;
    //song list
    private ArrayList<TrackListData> playList;
    //current position
    // private int position;


    private final IBinder musicBind = new MusicBinder();
    public boolean isCompleted;
    // public boolean isComplete;


    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return musicBind;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }


    public void onCreate() {
        //create the service


        super.onCreate();

    }

    //add a method to initialize the MediaPlayer class, after the onCreate method:
    public void initMusicPlayer(final int position) {

        if (mediaPlayer == null) mediaPlayer = new MediaPlayer();

        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.reset();
                // MediaPlayer mediaPlayer = MediaPlayer.create(context, URI.parse("file://" + filePath));
                mediaPlayer.setDataSource(playList.get(position).getPreviewUrl());
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                      public void onPrepared(MediaPlayer mp) {
                                                          mp.start();
                                                          Log.i("Ready", playList.get(position).getPreviewUrl());
                                                      }
                                                  }
                );


                mediaPlayer.setOnErrorListener(this);
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.prepareAsync();


            } catch (IllegalArgumentException | IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e("MUSIC SERVICE", "Completed");

        isCompleted = true;

    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e("MUSIC SERVICE", "Error not known");
        return false;
    }


    public ArrayList<TrackListData> getPlayList() {
        return playList;
    }

    public void setPlayList(ArrayList<TrackListData> playList) {
        this.playList = playList;
    }


    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }

    }

    public void restart() {

        mediaPlayer.start();


    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }


    public long getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }


    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;

        }
    }


}
