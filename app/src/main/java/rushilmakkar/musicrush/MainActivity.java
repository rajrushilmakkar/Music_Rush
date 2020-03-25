package rushilmakkar.musicrush;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterActivity;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView songsList;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songsList = findViewById(R.id.songsListView);
        seekPermission();
    }

    void listSongs() {
        final ArrayList<File> songs = searchSongs(Environment.getExternalStorageDirectory());
        items = new String[songs.size()];
        for (int i = 0; i < songs.size(); i++) {
            items[i] = songs.get(i).getName();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
            songsList.setAdapter(adapter);
            songsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String songname=songsList.getItemAtPosition(position).toString();
                    startActivity(new Intent(getApplicationContext(),musicplayer.class).putExtra("songs",songs).putExtra("songname",songname).putExtra("pos",position));
                }
            });
        }
    }

    ArrayList<File> searchSongs(File file) {
        ArrayList<File> songsArrayList = new ArrayList<>();
        File[] files = file.listFiles();
        for (File afile : files) {
            if (afile.isDirectory() && !afile.isHidden()) {
                songsArrayList.addAll(searchSongs(afile));
            } else {
                if (afile.getName().endsWith(".mp3") || afile.getName().endsWith(".wav")) {
                    songsArrayList.add(afile);
                }
            }
        }
        return songsArrayList;
    }

    void seekPermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                listSongs();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                //response.getRequestedPermission();
                //response.notify();

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }
}
