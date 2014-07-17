package com.pavlukhin.acropanda.game.tutorial;

import com.pavlukhin.acropanda.UserControlType;
import com.pavlukhin.acropanda.game.GameActivity;
import com.pavlukhin.acropanda.game.GameView;
import com.pavlukhin.acropanda.game.level.Prize;

/**
 * Created by Ivan on 06.06.2014.
 */
public class MessageTutorialGame extends GameView {
    private final TutorialMessages messages;
    public MessageTutorialGame(GameActivity context, TutorialMessages messages) {
        super(context);
        this.messages = messages;
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
