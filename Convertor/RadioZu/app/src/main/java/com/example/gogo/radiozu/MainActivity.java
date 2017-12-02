package com.example.gogo.radiozu;

import android.content.ComponentName;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button start;
    private MediaBrowserCompat mMediaBrowser;
    PlaybackStateCompat playbackStateCompat;
    PlaybackStateCompat pbState;
    private AudioBroadcastReceiver mHeadsetReceiver = new AudioBroadcastReceiver();
    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_HEADSET_PLUG);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mMediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MediaPlaybackService.class),
                mConnectionCallbacks,
                null);

        registerReceiver(mHeadsetReceiver,intentFilter);
    }
    @Override
    public void onStart() {
        super.onStart();
        mMediaBrowser.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        // (see "stay in sync with the MediaSession")
        if (MediaControllerCompat.getMediaController(MainActivity.this) != null) {
            MediaControllerCompat.getMediaController(MainActivity.this).unregisterCallback(controllerCallback);
        }
        mMediaBrowser.disconnect();

    }


    public class MediaPlayerActivity extends AppCompatActivity {
        private MediaBrowserCompat mMediaBrowser;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // ...
            // Create MediaBrowserServiceCompat
            mMediaBrowser = new MediaBrowserCompat(this,
                    new ComponentName(this, MediaPlaybackService.class),
                    mConnectionCallbacks,
                    null); // optional Bundle
        }

        @Override
        public void onStart() {
            super.onStart();
            mMediaBrowser.connect();
        }

        @Override
        public void onStop() {
            super.onStop();
            // (see "stay in sync with the MediaSession")
            if (MediaControllerCompat.getMediaController(MediaPlayerActivity.this) != null) {
                MediaControllerCompat.getMediaController(MediaPlayerActivity.this).unregisterCallback(controllerCallback);
            }
            mMediaBrowser.disconnect();
            unregisterReceiver(mHeadsetReceiver);
        }
    }


    private final MediaBrowserCompat.ConnectionCallback mConnectionCallbacks =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {

                    // Get the token for the MediaSession
                    MediaSessionCompat.Token token = mMediaBrowser.getSessionToken();

                    // Create a MediaControllerCompat
                    MediaControllerCompat mediaController =
                            null;
                    try {
                        mediaController = new MediaControllerCompat(MainActivity.this, // Context
                                token);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    // Save the controller
                    MediaControllerCompat.setMediaController(MainActivity.this, mediaController);

                    // Finish building the UI
                    buildTransportControls();
                }

                @Override
                public void onConnectionSuspended() {
                    // The Service has crashed. Disable transport controls until it automatically reconnects
                }

                @Override
                public void onConnectionFailed() {
                    // The Service has refused our connection
                }
            };

    void buildTransportControls()
    {
        // Grab the view for the play/pause button
        start = findViewById(R.id.start);

        // Attach a listener to the button
        start.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         // Since this is a play/pause button, you'll need to test the current state
                                         // and choose the action accordingly
                                         Log.e("STATE",playbackStateCompat+"");

                                         int pbState = MediaControllerCompat.getMediaController(MainActivity.this).getPlaybackState().getState();

                                         Log.e("STATE",pbState+"");
                                         if (pbState == PlaybackStateCompat.STATE_PLAYING) {
                                             MediaControllerCompat.getMediaController(MainActivity.this).getTransportControls().pause();
                                         } else {
                                             MediaControllerCompat.getMediaController(MainActivity.this).getTransportControls().play();
                                         }
                                     }
                                 });

            MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(MainActivity.this);


            // Display the initial state
            MediaMetadataCompat metadata = mediaController.getMetadata();
           pbState = mediaController.getPlaybackState();


            // Register a Callback to stay in sync
              mediaController.registerCallback(controllerCallback);


        }



    MediaControllerCompat.Callback controllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {}

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {
                    Log.e("da", "playback state changed: " + state.toString());
                }
            };



}
