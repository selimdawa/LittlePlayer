package com.flatcode.littleplayer.Fragment;

import static com.flatcode.littleplayer.Activity.MainActivity.albums;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.flatcode.littleplayer.Adapter.AlbumAdapter;
import com.flatcode.littleplayer.databinding.FragmentAlbumBinding;

public class AlbumFragment extends Fragment {

    private FragmentAlbumBinding binding;
    AlbumAdapter adapter;

    public AlbumFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAlbumBinding.inflate(LayoutInflater.from(getContext()), container, false);

        binding.recyclerView.setHasFixedSize(true);
        if (!(albums.size() < 1)) {
            adapter = new AlbumAdapter(getContext(), albums);
            binding.recyclerView.setAdapter(adapter);
        }
        return binding.getRoot();
    }
}