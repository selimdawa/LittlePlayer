package com.flatcode.littleplayer.Adapter;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littleplayer.Model.MusicFiles;
import com.flatcode.littleplayer.Unit.CLASS;
import com.flatcode.littleplayer.Unit.DATA;
import com.flatcode.littleplayer.Unit.VOID;
import com.flatcode.littleplayer.databinding.ItemMusicBinding;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.ViewHolder> {

    private ItemMusicBinding binding;
    private Context context;
    public static ArrayList<MusicFiles> albumFiles;

    public AlbumDetailsAdapter(Context context, ArrayList<MusicFiles> albumFiles) {
        this.context = context;
        this.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemMusicBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(albumFiles.get(position).getTitle());
        byte[] image = getAlbumArt(albumFiles.get(position).getPath());

        VOID.GlideByte(context, image, holder.image);
        VOID.GlideBlurByte(context, image, holder.imageBlur, 50);

        holder.itemView.setOnClickListener(v -> VOID.IntentExtra2Int(context, CLASS.PLAYER,
                DATA.SENDER, DATA.ALBUM_DETAILS, DATA.POSITION, position));
    }

    /*private void deleteFile(int position, View v) {
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(list.get(position).getId()));
        list.remove(position);
        File file = new File(list.get(position).getPath());
        boolean deleted = file.delete();
        if (deleted) {
            context.getContentResolver().delete(contentUri, null, null);
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
            Snackbar.make(v, "File Deleted", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(v, "Can't be Deleted", Snackbar.LENGTH_LONG).show();

        }
    }*/

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image,imageBlur;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = binding.name;
            image = binding.image;
            imageBlur = binding.imageBlur;
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