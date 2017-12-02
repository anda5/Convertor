package com.example.gogo.radiozu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

public class AudioBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        MediaPlayer player = MediaPlaybackService.player;
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    Log.d("HEADSET", "Headset is unplugged");
                    if(player!=null) {
                        player.start();
                    }
                    break;
                case 1:
                    Log.d("HEADSET", "Headset is plugged");
                    if(player!=null) {
                        player.pause();
                    }
                    break;
                default:
                    Log.d("HEADSET", "I have no idea what the headset state is");
            }
        }

    }
}
