package org.ivan.simple.game.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import org.ivan.simple.R;
import org.ivan.simple.game.level.LevelCell;
import org.ivan.simple.game.level.PlatformType;
import org.ivan.simple.game.motion.Motion;
import org.ivan.simple.game.motion.MotionType;

public class SoundManager {
	private SoundPool soundPool;
	private int jumpSoundId;
	private int flySoundId;
	private int dieSoundId;
	private int winSoundId;
	private AudioManager audioManager;
	
	public SoundManager(Context context) {
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		jumpSoundId = soundPool.load(context, R.raw.jump, 1);
		flySoundId = soundPool.load(context, R.raw.fly, 1);
		dieSoundId = soundPool.load(context, R.raw.war, 1);
        winSoundId = soundPool.load(context, R.raw.tick, 1);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

    public void playSound(Motion motion, Motion prevMotion, LevelCell nextCell, LevelCell prevCell) {
        MotionType mt = motion.getType();
        MotionType prevMt = prevMotion.getType();
        switch(mt) {
            case JUMP:
                if(motion.getStage() == 0) {
                    playSound(jumpSoundId);
                } else if (prevCell.getFloor().getType() == PlatformType.TRAMPOLINE) {
                    playSound(dieSoundId);
                }
                break;
            case JUMP_LEFT:
            case JUMP_RIGHT:
            case JUMP_LEFT_WALL:
            case JUMP_RIGHT_WALL:
                if(prevMt == MotionType.JUMP || prevMt == MotionType.TP) {
                } else {
                    playSound(jumpSoundId);
                }
                break;
            case FLY_LEFT:
            case FLY_RIGHT:
                if(motion.getStage() == 0) {
                    playSound(flySoundId);
                }
                break;
            case BEAT_ROOF:
                playSound(dieSoundId);
                break;
            case FALL:
            case FALL_BLANSH:
            case THROW_LEFT:
            case THROW_RIGHT:
            case STICK_LEFT:
            case STICK_RIGHT:
            case MAGNET:
            case TP:
            case TP_LEFT:
            case TP_RIGHT:
                break;
            default:
                playSound(jumpSoundId);
                break;
        }
    }

    public void playDetonate() {
        playSound(dieSoundId);
    }

    public void playWin() {
        playSound(winSoundId);
    }

    private void playSound(int soundId) {
        float volume = getVolume();
        soundPool.play(soundId, volume, volume, 1, 0, 1f);
    }

    private float getVolume() {
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return actualVolume / maxVolume;
    }
}
