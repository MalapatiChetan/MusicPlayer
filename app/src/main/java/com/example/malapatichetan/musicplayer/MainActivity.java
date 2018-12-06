package com.example.malapatichetan.musicplayer;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView artistImage;
    private MediaPlayer mediaPlayer;
    private TextView leftTime;
    private TextView rightTime;
   private SeekBar seekBar;
   private Button prevButton;
    private Button playButton;
    private Button nexrButton;
    private Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpUI();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    mediaPlayer.seekTo(i);
                }
                SimpleDateFormat dateFormat = new  SimpleDateFormat("mm:ss");

                int currentPosition = mediaPlayer.getCurrentPosition();

                int duration = mediaPlayer.getDuration();

                leftTime.setText(dateFormat.format(new Date(currentPosition)));
                rightTime.setText(dateFormat.format(new Date(duration - currentPosition)));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopMedia();

              //  int duration = mediaPlayer.getDuration();
              //  String mduration = String.valueOf(duration);
              //  changeButton();
              //  mediaPlayer.setOnCompletionListener(this);
                Toast.makeText(getApplicationContext(),"Media Player End.",Toast.LENGTH_SHORT).show();
                playButton.setBackgroundResource(android.R.drawable.ic_media_pause);


            }
        });
    }
    public void setUpUI(){

        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.mps3);


        artistImage = (ImageView) findViewById(R.id.imageView);
        leftTime = (TextView)findViewById(R.id.leftTime);
        rightTime = (TextView)findViewById(R.id.rightTime);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        prevButton = (Button)findViewById(R.id.prevButton);
        playButton = (Button)findViewById(R.id.playButton);
        nexrButton = (Button)findViewById(R.id.nextButton);

        prevButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        nexrButton.setOnClickListener(this);
    }



  /*  public void changeButton(){

        if (mediaPlayer != null ){
           // mediaPlayer.getDuration();

            playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }*/
    private void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.prevButton:
                backMusic();
                break;


            case R.id.playButton:
             if (mediaPlayer .isPlaying()){
                 pauseMusic();
                 //changeButton();

             }

             else {
                 startMusic();

             }




                break;

            case R.id.nextButton:
                nextMusic();

                break;

        }


        }
        public void pauseMusic(){
           if (mediaPlayer != null){
               mediaPlayer.pause();
               playButton.setBackgroundResource(android.R.drawable.ic_media_play);
           }

        }
        public void startMusic(){
            if (mediaPlayer != null){
                mediaPlayer.start();
                updateThread();
                playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
            }

        }
        public void updateThread(){
         thread = new Thread(){
             @Override
             public void run() {
                 try {
                     while (mediaPlayer != null && mediaPlayer.isPlaying()) {


                         Thread.sleep(50);
                         runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 int newPosition = mediaPlayer.getCurrentPosition();
                                 int newMax = mediaPlayer.getDuration();
                                 seekBar.setMax(newMax);
                                 seekBar.setProgress(newPosition);
                                 leftTime.setText(String.valueOf(new SimpleDateFormat("mm:ss").format(new Date(mediaPlayer.getCurrentPosition()))));
                                 rightTime.setText(String.valueOf(new SimpleDateFormat("mm:ss").format(new Date(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()))));
                             }
                         });
                     }
                 }catch (InterruptedException e){
                     e.printStackTrace();
                 }
             }
         };
         thread.start();

        }



        public void backMusic(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(0);
        }
        }
        public void nextMusic(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(mediaPlayer.getDuration()-1000);
        }
        }

    @Override
    protected void onDestroy() {
        if (mediaPlayer!= null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        thread.interrupt();
        thread = null;
        super.onDestroy();
    }
}
