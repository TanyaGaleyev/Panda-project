package com.pavlukhin.acropanda.game.tutorial;

import com.pavlukhin.acropanda.UserControlType;
import com.pavlukhin.acropanda.game.GameActivity;
import com.pavlukhin.acropanda.game.GameView;
import com.pavlukhin.acropanda.game.level.Prize;

/**
 * Created by ivan on 15.12.13.
 */
public class TutorialGame extends GameView {

    public TutorialGame(GameActivity context) {
        super(context);
    }

    @Override
    protected void updateGame(UserControlType controlType) {
        final Prize prize = level.model.getHeroCell().getPrize();
        if(prize != null && prize.getKind() >= 2) {
            getControl().postStopManager();
            post(new Runnable() {
                @Override
                public void run() {
                    getGameContext().startTutorial(prize.getKind());
                }
            });
        }
        super.updateGame(controlType);
    }
}
