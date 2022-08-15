package com.flatcode.littleplayer.Notification;

import static com.flatcode.littleplayer.Unit.ApplicationClass.ACTION_NEXT;
import static com.flatcode.littleplayer.Unit.ApplicationClass.ACTION_PLAY;
import static com.flatcode.littleplayer.Unit.ApplicationClass.ACTION_PREVIOUS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.flatcode.littleplayer.Unit.DATA;
import com.flatcode.littleplayer.Service.MusicService;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    String actionName = intent.getAction();
        Intent serviceIntent = new Intent(context, MusicService.class);
        if (actionName != null) {
            switch (actionName) {
                case ACTION_PLAY:
                    serviceIntent.putExtra(DATA.ACTION_NAME, DATA.PLAY_PAUSE);
                    context.startService(serviceIntent);
                    break;
                case ACTION_NEXT:
                    serviceIntent.putExtra(DATA.ACTION_NAME, DATA.NEXT);
                    context.startService(serviceIntent);
                    break;
                case ACTION_PREVIOUS:
                    serviceIntent.putExtra(DATA.ACTION_NAME, DATA.PREVIOUS);
                    context.startService(serviceIntent);
                    break;
            }
        }
    }
}