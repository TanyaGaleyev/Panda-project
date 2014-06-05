package org.ivan.simple.game.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseArray;

import org.ivan.simple.R;
import org.ivan.simple.game.level.LevelCell;
import org.ivan.simple.game.level.PlatformType;
import org.ivan.simple.game.motion.Motion;
import org.ivan.simple.game.motion.MotionType;

public class SoundManager {
    private SoundPool soundPool;
//    private int jumpSoundId;
//    private int flySoundId;
//    private int dieSoundId;
//    private int winSoundId;
//    private int warSoundId;
//    private int coinSoundId;
//    private int throwSoundId;
//    private int angleSoundId;
    private AudioManager audioManager;
    private SparseArray<Integer> soundMap = new SparseArray<Integer>();
    private final Context context;

    public SoundManager(Context context) {
        this.context = context;
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mapSound(R.raw.jump);
        mapSound(R.raw.fly);
        mapSound(R.raw.war);
        mapSound(R.raw.detonate);
        mapSound(R.raw.win);
        mapSound(R.raw.coin);
        mapSound(R.raw.throw0);
        mapSound(R.raw.angle);
        mapSound(R.raw.reduce);
        mapSound(R.raw.slick);
        mapSound(R.raw.tp);
    }

    private void mapSound(int resId) {
        soundMap.put(resId, soundPool.load(context, resId, 1));
    }

    public void playSound(Motion motion, Motion prevMotion, LevelCell nextCell, LevelCell prevCell) {
        MotionType mt = motion.getType();
        MotionType prevMt = prevMotion.getType();
        if(prevCell.getFloor().getType() == PlatformType.REDUCE) {
            playSound(R.raw.reduce);
            return;
        }
        switch(mt) {
            case JUMP:
                if(motion.getStage() == 0) {
                    playSound(R.raw.jump);
                } else if (prevCell.getFloor().getType() == PlatformType.TRAMPOLINE) {
                    playSound(R.raw.war);
                }
                break;
            case JUMP_LEFT:
            case JUMP_RIGHT:
                if(prevMt == MotionType.JUMP || prevMt == MotionType.TP) {
                } else if(prevCell.getFloor().getType() == PlatformType.ANGLE_LEFT ||
                prevCell.getFloor().getType() == PlatformType.ANGLE_RIGHT) {
                    playSound(R.raw.angle);
                } else if(prevCell.getFloor().getType() == PlatformType.SLICK) {
                    playSound(R.raw.slick);
                } else {
                    playSound(R.raw.jump);
                }
                break;
            case JUMP_LEFT_WALL:
            case JUMP_RIGHT_WALL:
                playSound(R.raw.coin);
                break;
            case FLY_LEFT:
            case FLY_RIGHT:
                if(motion.getStage() == 0) {
                    playSound(R.raw.fly);
                }
                break;
            case BEAT_ROOF:
                playSound(R.raw.war);
                break;
            case THROW_LEFT:
            case THROW_RIGHT:
                if(motion.getStage() == 0) {
                    playSound(R.raw.throw0);
                }
                break;
            case FALL:
            case FALL_BLANSH:
            case STICK_LEFT:
            case STICK_RIGHT:
            case MAGNET:
            case TP:
                break;
            case TP_LEFT:
            case TP_RIGHT:
                playSound(R.raw.tp);
                break;
            case STAY:
                if(prevCell.getFloor().getType() == PlatformType.BRICK ||
                        prevCell.getFloor().getType() == PlatformType.GLUE) {
                } else {
                    playSound(R.raw.jump);
                }
                break;
            default:
                playSound(R.raw.jump);
                break;
        }
    }

    public void playDetonate() {
        playSound(R.raw.detonate);
    }

    public void playWin() {
        playSound(R.raw.win);
    }

    private void playSound(int resId) {
        float volume = getVolume();
        soundPool.play(soundMap.get(resId), volume, volume, 1, 0, 1f);
    }

    private float getVolume() {
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return actualVolume / maxVolume;
    }
}
