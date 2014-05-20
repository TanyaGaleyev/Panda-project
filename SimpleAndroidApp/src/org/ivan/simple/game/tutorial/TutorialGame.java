package org.ivan.simple.game.tutorial;

import org.ivan.simple.game.GameActivity;
import org.ivan.simple.game.GameView;
import org.ivan.simple.game.level.Prize;

/**
 * Created by ivan on 15.12.13.
 */
public class TutorialGame extends GameView {

    public TutorialGame(GameActivity context) {
        super(context);
    }

    @Override
    protected void updateGame() {
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
        super.updateGame();
    }
}
