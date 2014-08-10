package com.pavlukhin.acropanda.game.level.actions;

import com.pavlukhin.acropanda.R;

/**
 * Created by ivan on 10.08.2014.
 */
public enum SoundActions implements SoundAction {
    PRIZE(R.raw.star),
    UNLOCK(R.raw.lock_platform_delete);

    private final int soundId;

    SoundActions(int soundId) {
        this.soundId = soundId;
    }

    @Override
    public int getSoundId() {
        return soundId;
    }
}
