package com.tikitoo.musicplayerair;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tipTv;
    private Button playBtn, pauseBtn, rePlayBtn, stopBtn;
    private SeekBar seekBar;
    MyPlayer player = null;
    private boolean pause = false;
    String url = "http://7xkjnc.dl1.z0.glb.clouddn.com/i_move_on_sintel_song.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        player = new MyPlayer(seekBar, url);
    }

    private void initViews() {
        tipTv = (TextView) findViewById(R.id.play_tips);
        playBtn = (Button) findViewById(R.id.play_start);
        playBtn.setOnClickListener(this);
        pauseBtn = (Button) findViewById(R.id.play_pause);
        pauseBtn.setOnClickListener(this);
        rePlayBtn = (Button) findViewById(R.id.play_replay);
        rePlayBtn.setOnClickListener(this);
        stopBtn = (Button) findViewById(R.id.play_stop);
        stopBtn.setOnClickListener(this);

        seekBar = (SeekBar) findViewById(R.id.seek_probressbar);
        seekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_start:
                player.play();
                tipTv.setText("Media start play");
                break;
            case R.id.play_pause:
                if (player.pause()) {
                    tipTv.setText("Media Continue play");
                    pauseBtn.setText(R.string.play_continue);
                } else {
                    tipTv.setText("Media pause play");
                    pauseBtn.setText(R.string.play_pause);
                }
                break;
            case R.id.play_stop:
                player.stop();
                tipTv.setText("Media stop play");
                break;
            case R.id.play_replay:
                player.rePlay();
                tipTv.setText("Media RePlay");
                break;
            default:
                break;
        }
    }


    private class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progressBar = 0;
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            this.progressBar = i * player.mediaPlayer.getDuration() / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            player.mediaPlayer.seekTo(progressBar);
        }
    }
}
