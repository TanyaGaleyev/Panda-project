package com.pavlukhin.acropanda.game.sound;

import android.content.Context;
import android.media.MediaPlayer;

import com.pavlukhin.acropanda.R;

public class MusicManager {
	
	private final Context context;
	private final MediaPlayer player;
    private boolean music = false;

    public MusicManager(Context context) {
		this.context = context;
		player = MediaPlayer.create(context, R.raw.drive);
		player.setLooping(true); // Set looping 
        player.setVolume(1, 1);
        player.stop();
	}

    public void setMusicEnabled(boolean enabled) {
        music = enabled;
        if (enabled)
            startPlayer();
        else
            player.stop();
    }

    public void setMusicPaused(boolean paused) {
        if(music) {
            if(paused) player.pause();
            else       player.start();
        }
    }

    private MediaPlayer.OnPreparedListener startPlayerCallback =
            new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
        }
    };
    public void startPlayer() {
        player.setOnPreparedListener(startPlayerCallback);
        player.prepareAsync();
    }

}
