package com.tikitoo.musicplayerair;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tikitoo on 7/23/15.
 */
public class MyPlayer implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener{
    private static final String TAG = MyPlayer.class.getName();
    public MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Timer mTimer = new Timer();
    private String musicUrl;
    private boolean pause;
    private int playPosition;

    public MyPlayer(SeekBar seekBar, String musicUrl) {
        this.seekBar = seekBar;
        this.musicUrl = musicUrl;

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            Log.d(TAG, "error: " + e);
        }

        mTimer.schedule(mTimeTask, 0, 1000);
    }

    TimerTask mTimeTask = new TimerTask() {
        @Override
        public void run() {
            if (mediaPlayer == null)
                return;
            if (mediaPlayer.isPlaying() && !seekBar.isPressed()) {
                handlerSeekBar.sendEmptyMessage(0);
            }
        }
    };

    Handler handlerSeekBar = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // super.handleMessage(msg);
            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
            if (duration > 0) {
                long pos = seekBar.getMax() * position / duration;
                seekBar.setProgress((int) pos);
            }
        }
    };

    public void play() {
        playStart(0);
    }

    public void rePlay() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.seekTo(0);
        else
        playStart(0);
    }

    public boolean pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            pause = true;
        } else {
            if (pause) {
                mediaPlayer.start();
                pause = false;
            }
        }
        return pause;
    }

    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }

    private void playStart(int playPosition) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicUrl);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(
                    new MyPreparedListener(playPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final class MyPreparedListener implements
            MediaPlayer.OnPreparedListener {
        private int playPosition;

        public MyPreparedListener(int playPosition) {
            this.playPosition = playPosition;
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
            if (playPosition > 0)
                mediaPlayer.seekTo(playPosition);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared");
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onCompletion");

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int bufferingBar) {
        seekBar.setSecondaryProgress(bufferingBar);
        int currentSeekBar = seekBar.getMax()
                * mediaPlayer.getCurrentPosition()
                / mediaPlayer.getDuration();
        Log.d(TAG, "onBufferingUpdate: " + "play: " + currentSeekBar
                + "; bufferingBar: " + bufferingBar);


    }




}
