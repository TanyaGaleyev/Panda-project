package com.pavlukhin.acropanda.game.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.pavlukhin.acropanda.R;
import com.pavlukhin.acropanda.game.GameManager;
import com.pavlukhin.acropanda.game.level.LevelCell;
import com.pavlukhin.acropanda.game.level.PlatformType;
import com.pavlukhin.acropanda.game.motion.Motion;
import com.pavlukhin.acropanda.game.motion.MotionType;

import java.util.Timer;
import java.util.TimerTask;

public class SoundManager {
    private SoundPool soundPool;
    private AudioManager audioManager;
    private final SoundMap soundMap;
    private final Context context;

    public SoundManager(Context context) {
        this.context = context;
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        soundMap = new SoundMap(context, soundPool);
    }

    public void playSound(Motion motion, Motion prevMotion, LevelCell nextCell, LevelCell prevCell) {
        MotionType mt = motion.getType();
        MotionType prevMt = prevMotion.getType();
        PlatformType prevFloor = prevCell.getFloor().getType();
        PlatformType prevLeft = prevCell.getLeft().getType();
        PlatformType prevRight = prevCell.getRight().getType();
        PlatformType prevRoof = prevCell.getRoof().getType();
        if(mt != MotionType.FLY_LEFT && mt != MotionType.FLY_RIGHT) {
            if (prevFloor == PlatformType.REDUCE
                    && (mt != MotionType.JUMP || motion.getStage() == 0)
                    && mt != MotionType.BEAT_ROOF && mt != MotionType.MAGNET) {
                if(!prevCell.getFloor().isAnimatingOrDelayed())
                    playSound(R.raw.reduce);
                return;
            }
            if (prevFloor == PlatformType.STRING
                    && (mt != MotionType.JUMP || motion.getStage() == 0)
                    && mt != MotionType.BEAT_ROOF && mt != MotionType.MAGNET) {
                if (mt == MotionType.FALL || mt == MotionType.FALL_BLANSH)
                    playSound(R.raw.string_break);
                else
                    playSound(R.raw.string);
                return;
            }
        }
        switch(mt) {
            case JUMP:
                if(motion.getStage() == 0 && prevFloor != PlatformType.SPIKE_UP) {
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
                } else if(prevLeft == PlatformType.UNLOCK) {
                    playSound(R.raw.lock_platform);
                } else if(prevFloor != PlatformType.SPIKE_UP && prevLeft != PlatformType.SPIKE_V) {
                    playSound(R.raw.coin);
                }
                break;
            case JUMP_RIGHT_WALL:
                if(prevRight == PlatformType.SWITCH) {
                    playSound(R.raw.switch0);
                } else if(prevRight == PlatformType.BRICK_V) {
                    playSound(R.raw.brick);
                } else if(prevRight == PlatformType.UNLOCK) {
                    playSound(R.raw.lock_platform);
                } else if(prevFloor != PlatformType.SPIKE_UP && prevRight != PlatformType.SPIKE_V) {
                    playSound(R.raw.coin);
                }
                break;
            case FLY_LEFT:
            case FLY_RIGHT:
                if(motion.getStage() == 0) {
                    if(prevMt == MotionType.JUMP ||
                            prevMt == MotionType.THROW_LEFT || prevMt == MotionType.THROW_RIGHT ||
                            prevMt == MotionType.FLY_LEFT || prevMt == MotionType.FLY_RIGHT)
                        playSound(R.raw.fly);
                    else
                        playDelayed(R.raw.fly, 4);
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
                } else if(prevRoof != PlatformType.SPIKE) {
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
            case TP_LR:
            case TP_RL:
                playSound(R.raw.tp);
                break;
            case STAY:
                if(prevFloor == PlatformType.GLUE) {
                    if(prevMt != MotionType.STAY)
                        playSound(R.raw.glue);
                } else if(prevFloor == PlatformType.BRICK) {
                    playSound(R.raw.brick);
                } else if(prevFloor != PlatformType.SPIKE_UP) {
                    playSound(R.raw.jump);
                }
                break;
            case TRY_JUMP_GLUE:
                playSound(R.raw.try_jump_from_glue);
                break;
            case CLOUD_IDLE:
            case CLOUD_LEFT:
            case CLOUD_RIGHT:
            case CLOUD_UP:
            case CLOUD_DOWN:
                break;
            default:
                playSound(R.raw.jump);
                break;
        }
    }

    private void jumpLR(MotionType prevMt, PlatformType prevFloor) {
        if(prevMt == MotionType.JUMP || prevMt == MotionType.TP || prevMt.isCLOUD()) {
        } else if(prevFloor == PlatformType.ANGLE_LEFT ||
                prevFloor == PlatformType.ANGLE_RIGHT) {
            playSound(R.raw.angle);
        } else if(prevFloor == PlatformType.SLICK) {
            playSound(R.raw.slick);
        } else if(prevFloor != PlatformType.SPIKE_UP) {
            playSound(R.raw.jump);
        }
    }

    public void playDetonate() {
        playSound(R.raw.detonate);
    }

    public void playWin() {
        playSound(R.raw.win);
    }

    public void playSound(int resId) {
        float volume = getVolume();
        soundPool.play(soundMap.get(resId), volume, volume, 1, 0, 1f);
    }

    private void playDelayed(final int resId, int framesDelay) {
        // TODO timer here is bad one
        TimerTask delayedPlayback = new TimerTask() {
            @Override
            public void run() {
                playSound(resId);
            }
        };
        new Timer().schedule(delayedPlayback, framesDelay * GameManager.getOneFrameDuration());
    }

    private float getVolume() {
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return actualVolume / maxVolume;
    }
}
