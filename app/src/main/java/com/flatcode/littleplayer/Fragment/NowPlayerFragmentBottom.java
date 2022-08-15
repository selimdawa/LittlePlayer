package com.flatcode.littleplayer.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.flatcode.littleplayer.Activity.MainActivity.ARTIST_TO_FRAG;
import static com.flatcode.littleplayer.Activity.MainActivity.PATH_TO_FRAG;
import static com.flatcode.littleplayer.Activity.MainActivity.SHOW_MINI_PLAYER;
import static com.flatcode.littleplayer.Activity.MainActivity.SONG_NAME_TO_FRAG;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.flatcode.littleplayer.R;
import com.flatcode.littleplayer.Service.MusicService;
import com.flatcode.littleplayer.databinding.FragmentNowPlayerBottomBinding;

import java.io.IOException;

public class NowPlayerFragmentBottom extends Fragment implements ServiceConnection {

    private FragmentNowPlayerBottomBinding binding;

    MusicService musicService;

    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_FILE = "STORED_MUSIC";
    public static final String ARTIST_NAME = "ARTIST NAME";
    public static final String SONG_NAME = "SONG NAME";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNowPlayerBottomBinding.inflate(LayoutInflater.from(getContext()), container, false);

        binding.nextBtn.setOnClickListener(v -> {
            if (musicService != null) {
                musicService.nextBtnClicked();
                if (getActivity() != null) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
                    editor.putString(MUSIC_FILE, musicService.musicFiles.get(musicService.position).getPath());
                    editor.putString(ARTIST_NAME, musicService.musicFiles.get(musicService.position).getArtist());
                    editor.putString(SONG_NAME, musicService.musicFiles.get(musicService.position).getTitle());
                    editor.apply();
                    SharedPreferences preferences = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
                    String path = preferences.getString(MUSIC_FILE, null);
                    String artistName = preferences.getString(ARTIST_NAME, null);
                    String songName = preferences.getString(SONG_NAME, null);
                    if (path != null) {
                        SHOW_MINI_PLAYER = true;
                        PATH_TO_FRAG = path;
                        ARTIST_TO_FRAG = artistName;
                        SONG_NAME_TO_FRAG = songName;
                    } else {
                        SHOW_MINI_PLAYER = false;
                        PATH_TO_FRAG = null;
                        ARTIST_TO_FRAG = null;
                        SONG_NAME_TO_FRAG = null;
                    }
                }
                if (SHOW_MINI_PLAYER) {
                    if (PATH_TO_FRAG != null) {
                        byte[] art = getAlbumArt(PATH_TO_FRAG);
                        if (art != null) {
                            Glide.with(requireContext()).load(art).into(binding.albumArt);
                        } else {
                            Glide.with(requireContext()).load(R.drawable.logo).into(binding.albumArt);
                        }
                        binding.name.setText(SONG_NAME_TO_FRAG);
                        binding.artist.setText(ARTIST_TO_FRAG);
                    }
                }
            }
        });

        binding.playPauseBtn.setOnClickListener(v -> {
            if (musicService != null) {
                musicService.playPauseBtnClicked();
                if (musicService.isPlaying()) {
                    binding.playPauseBtn.setImageResource(R.drawable.ic_pause);
                } else {
                    binding.playPauseBtn.setImageResource(R.drawable.ic_play);
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SHOW_MINI_PLAYER) {
            if (PATH_TO_FRAG != null) {
                byte[] art = getAlbumArt(PATH_TO_FRAG);
                if (art != null) {
                    Glide.with(requireContext()).load(art).into(binding.albumArt);
                } else {
                    Glide.with(requireContext()).load(R.drawable.logo).into(binding.albumArt);
                }
                binding.name.setText(SONG_NAME_TO_FRAG);
                binding.artist.setText(ARTIST_TO_FRAG);

                Intent intent = new Intent(getContext(), MusicService.class);
                if (getContext() != null) {
                    getContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //if (getContext() != null)
        //    getContext().unbindService(this);
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

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder binder = (MusicService.MyBinder) service;
        musicService = binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }
}