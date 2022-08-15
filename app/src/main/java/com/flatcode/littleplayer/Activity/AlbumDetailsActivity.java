package com.flatcode.littleplayer.Activity;

import static com.flatcode.littleplayer.Activity.MainActivity.musicFiles;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littleplayer.Adapter.AlbumDetailsAdapter;
import com.flatcode.littleplayer.Model.MusicFiles;
import com.flatcode.littleplayer.Unit.DATA;
import com.flatcode.littleplayer.Unit.THEME;
import com.flatcode.littleplayer.Unit.VOID;
import com.flatcode.littleplayer.databinding.ActivityAlbumDetailsBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AlbumDetailsActivity extends AppCompatActivity {

    private ActivityAlbumDetailsBinding binding;
    private final Context context = AlbumDetailsActivity.this;

    String albumName;
    ArrayList<MusicFiles> albumSongs = new ArrayList<>();
    AlbumDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Objects.requireNonNull(getSupportActionBar()).hide();
        albumName = getIntent().getStringExtra(DATA.ALBUM_NAME);
        int k = 0;
        for (int i = 0; i < musicFiles.size(); i++) {
            if (albumName.equals(musicFiles.get(i).getAlbum())) {
                albumSongs.add(k, musicFiles.get(i));
                k++;
            }
        }
        byte[] image = getAlbumArt(albumSongs.get(0).getPath());
        VOID.GlideByte(context, image, binding.image);
        VOID.GlideBlurByte(context, image, binding.imageBlur, 50);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(albumSongs.size() < 1)) {
            adapter = new AlbumDetailsAdapter(context, albumSongs);
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        }
    }

    private byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        try {
            retriever.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return art;
    }
}