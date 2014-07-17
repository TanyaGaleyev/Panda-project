package com.pavlukhin.acropanda.game.tutorial;

import com.pavlukhin.acropanda.UserControlType;
import com.pavlukhin.acropanda.game.GameActivity;
import com.pavlukhin.acropanda.game.GameView;
import com.pavlukhin.acropanda.game.level.Prize;

/**
 * Created by Ivan on 06.06.2014.
 */
public class MessageTutorialGame extends GameView {
    private TutorialMessages1 messages = new TutorialMessages1();
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
