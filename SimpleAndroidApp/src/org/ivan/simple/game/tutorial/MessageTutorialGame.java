package org.ivan.simple.game.tutorial;

import org.ivan.simple.UserControlType;
import org.ivan.simple.game.GameActivity;
import org.ivan.simple.game.GameView;
import org.ivan.simple.game.level.Prize;

/**
 * Created by Ivan on 06.06.2014.
 */
public class MessageTutorialGame extends GameView {
    private TutorialMessages messages = new TutorialMessages();
    public MessageTutorialGame(GameActivity context) {
        super(context);
    }

    @Override
    protected void updateGame(UserControlType controlType) {
        final Prize prize = level.model.getHeroCell().getPrize();
        if(prize != null && prize.getKind() >= 2) {
            getControl().toastMessage(messages.get(prize.getKind()));
        }
        super.updateGame(controlType);
    }
}
