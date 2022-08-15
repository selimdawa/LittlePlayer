package com.flatcode.littleplayer.Fragment;

import static com.flatcode.littleplayer.Activity.MainActivity.musicFiles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.flatcode.littleplayer.Adapter.MusicAdapter;
import com.flatcode.littleplayer.databinding.FragmentSongsBinding;

public class SongsFragment extends Fragment {

    private FragmentSongsBinding binding;
    public static MusicAdapter musicAdapter;

    public SongsFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSongsBinding.inflate(LayoutInflater.from(getContext()), container, false);

        binding.recyclerView.setHasFixedSize(true);
        if (!(musicFiles.size() < 1)) {
            musicAdapter = new MusicAdapter(getContext(), musicFiles);
            binding.recyclerView.setAdapter(musicAdapter);
        }
        return binding.getRoot();
    }
}