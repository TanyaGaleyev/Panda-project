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
        mapSound(R.raw.blansh);
        mapSound(R.raw.brick);
        mapSound(R.raw.switch0);
        mapSound(R.raw.open);
        mapSound(R.raw.magnet);
        mapSound(R.raw.trampoline);
        mapSound(R.raw.transparent);
        mapSound(R.raw.glue);
        mapSound(R.raw.string);
    }

    private void mapSound(int resId) {
        soundMap.put(resId, soundPool.load(context, resId, 1));
    }

    public void playSound(Motion motion, Motion prevMotion, LevelCell nextCell, LevelCell prevCell) {
        MotionType mt = motion.getType();
        MotionType prevMt = prevMotion.getType();
        PlatformType prevFloor = prevCell.getFloor().getType();
        PlatformType prevLeft = prevCell.getLeft().getType();
        PlatformType prevRight = prevCell.getRight().getType();
        PlatformType prevRoof = prevCell.getRoof().getType();
        if(prevFloor == PlatformType.REDUCE && (mt != MotionType.JUMP || motion.getStage() == 0)) {
            playSound(R.raw.reduce);
            return;
        }
        if(prevFloor == PlatformType.STRING && (mt != MotionType.JUMP || motion.getStage() == 0)) {
            playSound(R.raw.string);
            return;
        }
        switch(mt) {
            case JUMP:
                if(motion.getStage() == 0) {
                    playSound(R.raw.jump);
                } else if (prevFloor == PlatformType.TRAMPOLINE) {
                    playSound(R.raw.trampoline);
                } else if(prevRoof == PlatformType.ONE_WAY_UP || prevRoof == PlatformType.WAY_UP_DOWN) {
                    playSound(R.raw.open);
                } else if(prevRoof == PlatformType.TRANSPARENT) {
                    playSound(R.raw.transparent);
                }
                break;
            case JUMP_LEFT:
                if(prevLeft == PlatformType.LIMIT || prevLeft == PlatformType.ONE_WAY_LEFT) {
                    playSound(R.raw.open);
                } else if(prevLeft == PlatformType.TRANSPARENT_V) {
                    playSound(R.raw.transparent);
                } else {
                    jumpLR(prevMt, prevFloor);
                }
                break;
            case JUMP_RIGHT:
                if(prevRight == PlatformType.LIMIT || prevRight == PlatformType.ONE_WAY_RIGHT) {
                    playSound(R.raw.open);
                } else if(prevRight == PlatformType.TRANSPARENT_V) {
                    playSound(R.raw.transparent);
                } else {
                    jumpLR(prevMt, prevFloor);
                }
                break;
            case JUMP_LEFT_WALL:
                if(prevLeft == PlatformType.SWITCH) {
                    playSound(R.raw.switch0);
                } else if(prevLeft == PlatformType.BRICK_V) {
                    playSound(R.raw.brick);
                } else {
                    playSound(R.raw.coin);
                }
                break;
            case JUMP_RIGHT_WALL:
                if(prevRight == PlatformType.SWITCH) {
                    playSound(R.raw.switch0);
                } else if(prevRight == PlatformType.BRICK_V) {
                    playSound(R.raw.brick);
                } else if(prevRight == PlatformType.LIMIT) {
                    playSound(R.raw.open);
                } else {
                    playSound(R.raw.coin);
                }
                break;
            case FLY_LEFT:
            case FLY_RIGHT:
                if(motion.getStage() == 0) {
                    playSound(R.raw.fly);
                } else if(prevLeft == PlatformType.ONE_WAY_LEFT || prevRight == PlatformType.ONE_WAY_RIGHT ||
                        prevLeft == PlatformType.LIMIT || prevRight == PlatformType.LIMIT) {
                    playSound(R.raw.open);
                } else if(prevLeft == PlatformType.TRANSPARENT_V || prevRight == PlatformType.TRANSPARENT_V) {
                    playSound(R.raw.transparent);
                }
                break;
            case BEAT_ROOF:
                if(prevRoof == PlatformType.BRICK) {
                    playSound(R.raw.brick);
                } else {
                    playSound(R.raw.war);
                }
                break;
            case THROW_LEFT:
            case THROW_RIGHT:
                if(motion.getStage() == 0) {
                    playSound(R.raw.throw0);
                }
                break;
            case FALL:
                if(prevFloor == PlatformType.ONE_WAY_DOWN || prevFloor == PlatformType.WAY_UP_DOWN) {
                    playSound(R.raw.open);
                } else if(prevFloor == PlatformType.TRANSPARENT) {
                    playSound(R.raw.transparent);
                }
                break;
            case FALL_BLANSH:
                if(prevFloor == PlatformType.TRANSPARENT) {
                    playSound(R.raw.transparent);
                }
//                playSound(R.raw.blansh);
                break;
            case STICK_LEFT:
            case STICK_RIGHT:
                if(motion.getStage() == 0)
                    playSound(R.raw.glue);
                break;
            case MAGNET:
                playSound(R.raw.magnet);
                break;
            case TP:
                if(motion.getStage() == 0)
                    playSound(R.raw.tp);
                break;
            case TP_LEFT:
            case TP_RIGHT:
                playSound(R.raw.tp);
                break;
            case STAY:
                if(prevFloor == PlatformType.GLUE) {
                    if(prevMt != MotionType.STAY)
                        playSound(R.raw.glue);
                } else if(prevFloor == PlatformType.BRICK) {
                    playSound(R.raw.brick);
                } else {
                    playSound(R.raw.jump);
                }
                break;
            default:
                playSound(R.raw.jump);
                break;
        }
    }

    private void jumpLR(MotionType prevMt, PlatformType prevFloor) {
        if(prevMt == MotionType.JUMP || prevMt == MotionType.TP) {
        } else if(prevFloor == PlatformType.ANGLE_LEFT ||
                prevFloor == PlatformType.ANGLE_RIGHT) {
            playSound(R.raw.angle);
        } else if(prevFloor == PlatformType.SLICK) {
            playSound(R.raw.slick);
        } else {
            playSound(R.raw.jump);
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
