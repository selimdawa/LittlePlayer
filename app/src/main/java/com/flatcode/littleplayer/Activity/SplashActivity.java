package com.flatcode.littleplayer.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littleplayer.Unit.CLASS;
import com.flatcode.littleplayer.Unit.THEME;
import com.flatcode.littleplayer.Unit.VOID;
import com.flatcode.littleplayer.databinding.ActivitySplashBinding;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    Context context = SplashActivity.this;

    int time_per_second = 2;
    final static int time_per_millis = 1000;
    int time_final = time_per_millis * time_per_second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Objects.requireNonNull(getSupportActionBar()).hide();

        VOID.Logo(getBaseContext(), binding.logo);
        VOID.Intro(getBaseContext(), binding.background, binding.backWhite, binding.backBlack);

        new Handler().postDelayed(this::checkUser, time_final);
    }

    private void checkUser() {
        VOID.IntentClear(context, CLASS.MAIN);
    }
}