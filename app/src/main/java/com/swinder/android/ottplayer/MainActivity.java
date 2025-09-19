package com.swinder.android.ottplayer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.util.UnstableApi;

import android.util.Log;

import com.swinder.android.media3player.Media3Player;
import com.swinder.android.ottplayer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements Media3Player.Callback {
    private static final String TAG = "OTTPlayer";
    private ActivityMainBinding binding;
    private Media3Player mPlayer;
    private boolean isRunning;

    public int getManifestType(String url) {
        if (url.contains(".mpd")) {
            return Media3Player.SOURCE_TYPE_MPEG_DASH;
        } else if (url.contains(".m3u8")) {
            return Media3Player.SOURCE_TYPE_HLS;
        }
        return Media3Player.SOURCE_TYPE_MPEG_DASH;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @UnstableApi
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "> PlaybackActivity onResume()");
        isRunning = true;
        play();
        Log.d(TAG, "< PlaybackActivity onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "> PlaybackActivity onPause()");
        isRunning = false;
        releasePlayer();
        Log.d(TAG, "< PlaybackActivity onPause()");
    }

    @UnstableApi
    @Override
    public void resetPlayer() {
        Log.d(TAG, "> PlaybackActivity resetPlayer()");
        releasePlayer();
        play();
        Log.d(TAG, "< PlaybackActivity resetPlayer()");
    }

    private void releasePlayer() {
        Log.d(TAG, "> PlaybackActivity releasePlayer() player: " + mPlayer);
        if(mPlayer != null) {
            mPlayer.setPlayWhenReady(false);
            mPlayer.release();
            mPlayer = null;
        }
        Log.d(TAG, "< PlaybackActivity releasePlayer()");
    }

    @UnstableApi
    private void play() {
        Log.d(TAG, "> PlaybackActivity play()");
        releasePlayer();
        if (!isRunning) {
            Log.d(TAG, "< PlaybackActivity play() Activity not in running state");
            return;
        }
        String streamUrl = "https://bitmovin-a.akamaihd.net/content/sintel/hls/playlist.m3u8";
        String license = null;
        //String streamUrl = "https://dash.akamaized.net/dash264/TestCasesHD/2b/qualcomm/1/MultiResMPEG2.mpd";
        mPlayer = new Media3Player(this, binding.playerView, this, getManifestType(streamUrl),
                streamUrl, license, null, false, null, null, null);
        Log.d(TAG, "< PlaybackActivity play()");
    }
}