package com.flatcode.littleplayer.Unit;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.flatcode.littleplayer.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class VOID {

    public static void IntentClear(Context context, Class c) {
        Intent intent = new Intent(context, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void Intent(Context context, Class c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    public static void IntentExtra(Context context, Class c, String key, String value) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        context.startActivity(intent);
    }

    public static void IntentExtraInt(Context context, Class c, String key, int value) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        context.startActivity(intent);
    }

    public static void IntentExtra2(Context context, Class c, String key, String value, String key2, String value2) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        intent.putExtra(key2, value2);
        context.startActivity(intent);
    }


    public static void IntentExtra2Int(Context context, Class c, String key, String value, String key2, int value2) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        intent.putExtra(key2, value2);
        context.startActivity(intent);
    }

    public static void Glide(Context context, String Url, ImageView Image) {
        try {
            if (Url != null)
                Glide.with(context).load(Url).placeholder(R.color.image_profile).into(Image);
            else
                Glide.with(context).load(R.drawable.logo).into(Image);
        } catch (Exception e) {
            Image.setImageResource(R.drawable.logo);
        }
    }

    public static void GlideByte(Context context, byte[] Url, ImageView Image) {
        try {
            if (Url != null)
                Glide.with(context).load(Url).placeholder(R.color.image_profile).into(Image);
            else
                Glide.with(context).load(R.drawable.logo).into(Image);
        } catch (Exception e) {
            Image.setImageResource(R.drawable.logo);
        }
    }

    public static void GlideBitmap(Context context, Bitmap Url, ImageView Image) {
        try {
            if (Url != null)
                Glide.with(context).load(Url).placeholder(R.color.image_profile).into(Image);
            else
                Glide.with(context).load(R.drawable.logo).into(Image);
        } catch (Exception e) {
            Image.setImageResource(R.drawable.logo);
        }
    }

    public static void GlideBlurBitmap(Context context, Bitmap Url, ImageView Image, int level) {
        try {
            if (Url != null)
            Glide.with(context).load(Url).placeholder(R.color.image_profile)
                    .apply(bitmapTransform(new BlurTransformation(level))).into(Image);
            else
                Glide.with(context).load(R.drawable.logo)
                        .apply(bitmapTransform(new BlurTransformation(level))).into(Image);
        } catch (Exception e) {
            Image.setImageResource(R.drawable.logo);
        }
    }

    public static void GlideBlurByte(Context context, byte[] Url, ImageView Image, int level) {
        try {
            if (Url != null)
            Glide.with(context).load(Url).placeholder(R.color.image_profile)
                    .apply(bitmapTransform(new BlurTransformation(level))).into(Image);
            else
                Glide.with(context).load(R.drawable.logo)
                        .apply(bitmapTransform(new BlurTransformation(level))).into(Image);
        } catch (Exception e) {
            Image.setImageResource(R.drawable.logo);
        }
    }

    public static void Intro(Context context, ImageView background, ImageView backWhite, ImageView backDark) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (sharedPreferences.getString("color_option", "ONE").equals("ONE")) {
            background.setImageResource(R.drawable.background_day);
            backWhite.setVisibility(View.VISIBLE);
            backDark.setVisibility(View.GONE);
        } else if (sharedPreferences.getString("color_option", "NIGHT_ONE").equals("NIGHT_ONE")) {
            background.setImageResource(R.drawable.background_night);
            backWhite.setVisibility(View.GONE);
            backDark.setVisibility(View.VISIBLE);
        }
    }

    public static void Logo(Context context, ImageView background) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (sharedPreferences.getString("color_option", "ONE").equals("ONE")) {
            background.setImageResource(R.drawable.logo);
        } else if (sharedPreferences.getString("color_option", "NIGHT_ONE").equals("NIGHT_ONE")) {
            background.setImageResource(R.drawable.logo_night);
        }
    }
}