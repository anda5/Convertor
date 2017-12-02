package com.example.gogo.radiozu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.service.media.MediaBrowserService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import static android.media.session.PlaybackState.PLAYBACK_POSITION_UNKNOWN;
import static android.media.session.PlaybackState.STATE_PAUSED;
import static android.media.session.PlaybackState.STATE_PLAYING;
import static android.support.v4.widget.ViewDragHelper.STATE_IDLE;

/**
 * Created by GoGo on 11/19/2017.
 */

public class MediaPlaybackService extends MediaBrowserServiceCompat {

    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

    // Defined elsewhere...
    private AudioManager.OnAudioFocusChangeListener afChangeListener;
    private AudioBroadcastReceiver myNoisyAudioStreamReceiver = new AudioBroadcastReceiver();
    public static MediaPlayer player;




    @Override
    public void onCreate() {
        super.onCreate();


        // Create a MediaSessionCompat
        mMediaSession = new MediaSessionCompat(this.getApplicationContext(), "MEDIA_SESION");

        // Enable callbacks from MediaButtons and TransportControls
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        setMediaPlaybackState(PlaybackStateCompat.STATE_NONE);

        // MySessionCallback() has methods that handle callbacks from a media controller
        mMediaSession.setCallback(callback);


        // Set the session's token so that client activities can communicate with it.
        setSessionToken(mMediaSession.getSessionToken());

    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return new BrowserRoot(
                getString(R.string.app_name), // Name visible in Android Auto
                null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.sendResult(null);

    }




        MediaSessionCompat.Callback callback = new
                MediaSessionCompat.Callback() {
                    @Override
                    public void onPlay() {
                        AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                        // Request audio focus for playback, this registers the afChangeListener
                        int result = am.requestAudioFocus(afChangeListener,
                                // Use the music stream.
                                AudioManager.STREAM_MUSIC,
                                // Request permanent focus.
                                AudioManager.AUDIOFOCUS_GAIN);

                        Log.e("INTRA2","AICI");
                        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                            // Start the service
                            start();
                            // Set the session active  (and update metadata and state)
                            mMediaSession.setActive(true);

                                player = new MediaPlayer();
                                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                try {
                                    player.setDataSource("https://www.ssaurel.com/tmp/mymusic.mp3");
                                    player.prepareAsync();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            // start the player (custom call)
                            Log.e("INTRA","PLAYYYYY");
                            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    player.start();
                                    setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING);
                                }
                            });

                            // Register BECOME_NOISY BroadcastReceiver
                          //  registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
                            // Put the service in the foreground, post notification

                        }
                    }

                    @Override
                    public void onStop() {
                        AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                        // Abandon audio focus
                        am.abandonAudioFocus(afChangeListener);
                       // unregisterReceiver(myNoisyAudioStreamReceiver);
                        // Start the service
                        stop();
                        // Set the session inactive  (and update metadata and state)
                        mMediaSession.setActive(false);
                        // stop the player (custom call)
                        player.stop();
                        // Take the service out of the foreground
                        stopForeground(false);
                    }

                    @Override
                    public void onPause() {
                        AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                        // Update metadata and state
                        // pause the player (custom call)
                        Log.e("INTRA2","PAUZA");

                        if(player!=null) {
                            player.pause();
                        }
                        setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED);

                        // unregister BECOME_NOISY BroadcastReceiver
                       // unregisterReceiver(myNoisyAudioStreamReceiver);
                        // Take the service out of the foreground, retain the notification
                        stopForeground(false);
                    }
                };




    private void start() {
        startService(new Intent(this, MediaPlaybackService.class));
    }

    private void stop() {
        stopService(new Intent(this, MediaPlaybackService.class));
    }



    private long getAvailableActions() {
        long actions = PlaybackState.ACTION_PLAY |
                PlaybackState.ACTION_PAUSE |
                PlaybackState.ACTION_PLAY_PAUSE |
                PlaybackState.ACTION_REWIND |
                PlaybackState.ACTION_FAST_FORWARD |
                PlaybackState.ACTION_SKIP_TO_PREVIOUS |
                PlaybackState.ACTION_SKIP_TO_NEXT |
                PlaybackState.ACTION_PLAY_FROM_MEDIA_ID |
                PlaybackState.ACTION_PLAY_FROM_SEARCH;
        return actions;
    }

    private void setMediaPlaybackState(int state) {
        PlaybackStateCompat.Builder playbackstateBuilder = new PlaybackStateCompat.Builder();
        if( state == PlaybackStateCompat.STATE_PLAYING ) {
            playbackstateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_PAUSE);
        } else {
            playbackstateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_PLAY);
        }
        playbackstateBuilder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0);
        mMediaSession.setPlaybackState(playbackstateBuilder.build());
    }

}
