package com.flatcode.littleplayer.Activity;

import static com.flatcode.littleplayer.Activity.MainActivity.repeatBoolean;
import static com.flatcode.littleplayer.Activity.MainActivity.shuffleBoolean;
import static com.flatcode.littleplayer.Adapter.AlbumDetailsAdapter.albumFiles;
import static com.flatcode.littleplayer.Adapter.MusicAdapter.mFiles;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littleplayer.Model.MusicFiles;
import com.flatcode.littleplayer.R;
import com.flatcode.littleplayer.Service.MusicService;
import com.flatcode.littleplayer.Unit.ActionPlaying;
import com.flatcode.littleplayer.Unit.DATA;
import com.flatcode.littleplayer.Unit.THEME;
import com.flatcode.littleplayer.Unit.VOID;
import com.flatcode.littleplayer.databinding.ActivityPlayerBinding;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection {

    private ActivityPlayerBinding binding;
    private Context context = PlayerActivity.this;

    private int position = -1;
    public static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;
    MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        setFullScreen();
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getIntentMethod();

        binding.back.setOnClickListener(v -> onBackPressed());
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicService != null && fromUser)
                    musicService.seekTo(progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    binding.seekBar.setProgress(mCurrentPosition);
                    binding.durationPlayed.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });
        binding.shuffle.setOnClickListener(v -> {
            if (shuffleBoolean) {
                shuffleBoolean = false;
                binding.shuffle.setImageResource(R.drawable.ic_shuffle_off);
            } else {
                shuffleBoolean = true;
                binding.shuffle.setImageResource(R.drawable.ic_shuffle_on);
            }
        });
        binding.repeat.setOnClickListener(v -> {
            if (repeatBoolean) {
                repeatBoolean = false;
                binding.repeat.setImageResource(R.drawable.ic_repeat_off);
            } else {
                repeatBoolean = true;
                binding.repeat.setImageResource(R.drawable.ic_repeat_on);
            }
        });
    }

    private void setFullScreen() {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(context, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    private void prevThreadBtn() {
        prevThread = new Thread() {
            @Override
            public void run() {
                super.run();
                binding.prev.setOnClickListener(v -> prevBtn());
            }
        };
        prevThread.start();
    }

    public void prevBtn() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            binding.songName.setText(listSongs.get(position).getTitle());
            binding.songArtist.setText(listSongs.get(position).getArtist());
            binding.seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.onCompleted();
            binding.playPause.setBackgroundResource(R.drawable.ic_pause);
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            binding.songName.setText(listSongs.get(position).getTitle());
            binding.songArtist.setText(listSongs.get(position).getArtist());
            binding.seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.onCompleted();
            binding.playPause.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private void nextThreadBtn() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                binding.next.setOnClickListener(v -> nextBtn());
            }
        };
        nextThread.start();
    }

    public void nextBtn() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();
            if (shuffleBoolean && !repeatBoolean)
                position = getRandom(listSongs.size() - 1);
            else if (!shuffleBoolean && !repeatBoolean)
                position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            binding.songName.setText(listSongs.get(position).getTitle());
            binding.songArtist.setText(listSongs.get(position).getArtist());
            binding.seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.onCompleted();
            binding.playPause.setBackgroundResource(R.drawable.ic_pause);
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();
            if (shuffleBoolean && !repeatBoolean)
                position = getRandom(listSongs.size() - 1);
            else if (!shuffleBoolean && !repeatBoolean)
                position = ((position + 1) % listSongs.size());

            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            binding.songName.setText(listSongs.get(position).getTitle());
            binding.songArtist.setText(listSongs.get(position).getArtist());
            binding.seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.onCompleted();
            binding.playPause.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);
    }

    private void playThreadBtn() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                binding.playPauseBtn.setOnClickListener(v -> playPauseBtn());
            }
        };
        playThread.start();
    }

    public void playPauseBtn() {
        if (musicService.isPlaying()) {
            binding.playPause.setImageResource(R.drawable.ic_play);
            musicService.pause();
            binding.seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        } else {
            binding.playPause.setImageResource(R.drawable.ic_pause);
            musicService.start();
            binding.seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        binding.seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    private String formattedTime(int currentPosition) {
        String totalOut = DATA.EMPTY;
        String totalNew = DATA.EMPTY;
        String seconds = String.valueOf(currentPosition % 60);
        String minutes = String.valueOf(currentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1)
            return totalNew;
        else
            return totalOut;
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra(DATA.POSITION, -1);
        String sender = getIntent().getStringExtra(DATA.SENDER);
        if (sender != null && sender.equals(DATA.ALBUM_DETAILS))
            listSongs = albumFiles;
        else
            listSongs = mFiles;

        if (listSongs != null) {
            binding.playPause.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        Intent intent = new Intent(context, MusicService.class);
        intent.putExtra(DATA.SERVICE_POSITION, position);
        startService(intent);
    }

    private void metaData(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
        binding.durationTotal.setText(formattedTime(durationTotal));
        byte[] art = retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if (art != null) {
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            VOID.GlideBitmap(context, bitmap, binding.image);
            VOID.GlideBlurBitmap(context, bitmap, binding.imageBlur, 10);
        } else {
            VOID.Glide(context, null, binding.image);
            binding.songName.setTextColor(Color.WHITE);
            binding.songArtist.setTextColor(Color.DKGRAY);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.getService();
        musicService.setCallBack(this);
        Toast.makeText(context, "Connected" + musicService, Toast.LENGTH_SHORT).show();
        binding.seekBar.setMax(musicService.getDuration() / 1000);
        metaData(uri);
        binding.songName.setText(listSongs.get(position).getTitle());
        binding.songArtist.setText(listSongs.get(position).getArtist());
        musicService.onCompleted();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }
}