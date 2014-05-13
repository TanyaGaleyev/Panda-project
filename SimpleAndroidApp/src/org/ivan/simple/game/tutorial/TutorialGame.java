package org.ivan.simple.game.tutorial;

import org.ivan.simple.game.GameActivity;
import org.ivan.simple.game.GameView;

/**
 * Created by ivan on 15.12.13.
 */
public class TutorialGame extends GameView {

    public TutorialGame(GameActivity context) {
        super(context);
    }

    @Override
    protected void updateGame() {
        if(level.model.getHeroCell().getPrize() != null) {
            getControl().postStopManager();
            post(new Runnable() {
                @Override
                public void run() {
                    getGameContext().startTutorial();
                }
            });
        }
        super.updateGame();
    }
}
