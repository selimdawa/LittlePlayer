package com.flatcode.littleplayer.Adapter;

import android.content.ContentUris;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littleplayer.Model.MusicFiles;
import com.flatcode.littleplayer.R;
import com.flatcode.littleplayer.Unit.CLASS;
import com.flatcode.littleplayer.Unit.DATA;
import com.flatcode.littleplayer.Unit.VOID;
import com.flatcode.littleplayer.databinding.ItemMusicBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private ItemMusicBinding binding;
    private Context context;
    public static ArrayList<MusicFiles> mFiles;

    public MusicAdapter(Context context, ArrayList<MusicFiles> mFiles) {
        this.context = context;
        this.mFiles = mFiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemMusicBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(mFiles.get(position).getTitle());
        byte[] image = getAlbumArt(mFiles.get(position).getPath());

        VOID.GlideByte(context, image, holder.image);
        VOID.GlideBlurByte(context, image, holder.imageBlur, 50);

        holder.itemView.setOnClickListener(v -> VOID.IntentExtraInt(context, CLASS.PLAYER, DATA.POSITION, position));

        holder.more.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.delete:
                        //temp = new list.get(position).getName(),list.get(position).getImg();
                        Toast.makeText(context, "Delete Clicked!!", Toast.LENGTH_SHORT).show();
                        deleteFile(position, v);
                        break;
                }
                return true;
            });
        });
    }

    private void deleteFile(int position, View v) {
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(mFiles.get(position).getId()));
        File file = new File(mFiles.get(position).getPath());
        boolean deleted = file.delete();
        if (deleted) {
            context.getContentResolver().delete(contentUri, null, null);
            mFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mFiles.size());
            Snackbar.make(v, "File Deleted : ", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(v, "Can't be Deleted : ", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image, more, imageBlur;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = binding.name;
            image = binding.image;
            imageBlur = binding.imageBlur;
            more = binding.more;
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

    public void updateList(ArrayList<MusicFiles> musicFilesArrayList) {
        mFiles = new ArrayList<>();
        mFiles.addAll(musicFilesArrayList);
        notifyDataSetChanged();
    }
}