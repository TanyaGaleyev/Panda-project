package org.ivan.simple.game.sound;

import android.content.Context;
import android.media.MediaPlayer;

import org.ivan.simple.PandaApplication;
import org.ivan.simple.R;

public class MusicManager {
	
	private final Context context;
	private final MediaPlayer player;
	
	public MusicManager(Context context) {
		this.context = context;
		player = MediaPlayer.create(context, R.raw.drive);
		player.setLooping(true); // Set looping 
        player.setVolume(1, 1); 
	}
	
	public void pauseMusic() {
		player.pause();
	}
	
	public void resumeMusic() {
		if(PandaApplication.getPandaApplication().getSound()) {
			player.start();
		}
	}

}
