package rushilmakkar.musicrush;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class musicplayer extends AppCompatActivity {
    Button next,previous,play;
    TextView song_name;
    String sname;
    String songname;
    SeekBar seekBar;
    static MediaPlayer player;
    int position;
    ArrayList<File> songs;
    Thread runseekBar;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);
        next=findViewById(R.id.next);
        previous=findViewById(R.id.previous);
        play=findViewById(R.id.play);
        song_name=findViewById(R.id.song_name);
        seekBar=findViewById(R.id.seekbar);
        getSupportActionBar().setTitle("Enjoy the music!");
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        runseekBar=new Thread(){
            @Override
            public void run() {
                int duration=player.getDuration();
                int currentpos=0;
                while(currentpos<duration){
                    try{
                        sleep(800 );
                        currentpos=player.getCurrentPosition();
                        seekBar.setProgress(currentpos);

                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        };
        if(player!=null){
            player.stop();
            player.release();
        }
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        songs=(ArrayList)bundle.getParcelableArrayList("songs");
        songname=songs.get(position).getName().toString();
        sname=i.getStringExtra("songname");
        song_name.setText(sname);
        song_name.setSelected(true);

        position=bundle.getInt("pos",0);
        Uri u=Uri.parse(songs.get(position).toString());
        player=MediaPlayer.create(getApplicationContext(),u);
        player.start();
        seekBar.setMax(player.getDuration());
        runseekBar.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onBackPressed();
                //next.callOnClick();
                //runseekBar.start();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setMax(player.getDuration());
                if(player.isPlaying()){
                    play.setBackgroundResource(R.drawable.play);
                    player.pause();
                }
                else{
                    play.setBackgroundResource(R.drawable.pause);
                    player.start();
                }
            }

        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
                player.release();
                position=((position+1)%songs.size());
                Uri u=Uri.parse(songs.get(position).toString());
                player=MediaPlayer.create(getApplicationContext(),u);
                songname=songs.get(position).getName();
                song_name.setText(songname);
                player.start();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
                player.release();
                position=((position-1)<0)?(songs.size()-1):(position-1);
                Uri u=Uri.parse(songs.get(position).toString());
                player=MediaPlayer.create(getApplicationContext(),u);
                songname=songs.get(position).getName();
                song_name.setText(songname);
                player.start();

                //runseekBar.start();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
