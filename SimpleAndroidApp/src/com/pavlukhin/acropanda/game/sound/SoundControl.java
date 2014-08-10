package com.pavlukhin.acropanda.game.sound;

import com.pavlukhin.acropanda.PandaBaseActivity;
import com.pavlukhin.acropanda.game.level.LevelCell;
import com.pavlukhin.acropanda.game.level.actions.SoundAction;
import com.pavlukhin.acropanda.game.motion.Motion;
import com.pavlukhin.acropanda.utils.OneShotAction;

/**
 * Created by Ivan on 20.06.2014.
 */
public class SoundControl {
    private final SoundManager sounds;
    private final PandaBaseActivity context;
    private OneShotAction playWinSound;
    private OneShotAction playLoseSound;

    public SoundControl(PandaBaseActivity context) {
        this.context = context;
        this.sounds = context.app().getSoundManager();
        init();
    }

    public void init() {
        playWinSound = new OneShotAction() {
            @Override
            protected void doAction() {
                if (context.app().getSound()) {
                    sounds.playWin();
                }
            }
        };
        playLoseSound = new OneShotAction() {
            @Override
            protected void doAction() {
                if (context.app().getSound()) {
                    sounds.playDetonate();
                }
            }
        };
    }

    public void playDetonateSound() {
        playLoseSound.act();
    }

    public void playWinSound() {
        playWinSound.act();
    }

    public void playSound(Motion motion, Motion prevMotion, LevelCell nextCell, LevelCell prevCell) {
        if(context.app().getSound()) {
            sounds.playSound(motion, prevMotion, nextCell, prevCell);
        }
    }

    public void playSound(SoundAction action) {
        if(context.app().getSound())
            sounds.playSound(action.getSoundId());
    }
}
