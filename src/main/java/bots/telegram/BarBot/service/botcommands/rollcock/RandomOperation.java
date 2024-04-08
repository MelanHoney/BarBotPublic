package bots.telegram.BarBot.service.botcommands.rollcock;

import bots.telegram.BarBot.model.UserGroup;
import bots.telegram.BarBot.service.BarBotSendMessageService;
import bots.telegram.BarBot.service.botcommands.rollcock.cocktypes.CockTypes;
import bots.telegram.BarBot.service.botcommands.rollcock.operations.*;

import java.util.Random;

public class RandomOperation {
    private final BarBotSendMessageService barBotSendMessageService;
    private final UserGroup userGroupData;

    public RandomOperation(BarBotSendMessageService barBotSendMessageService, UserGroup userGroupData) {
        this.barBotSendMessageService = barBotSendMessageService;
        this.userGroupData = userGroupData;
    }

    public CockOperation bySize(int cockSize) {
        CockTypes cockType = defineCockTypeBySize(cockSize);

        return switch (cockType) {
            case ZERO -> zeroSizeRandom();
            case SMALL -> smallSizeRandom();
            case MEDIUM -> mediumSizeRandom();
            case BIG -> bigSizeRandom();
            case HUGE -> hugeSizeRandom();
        };
    }

    private CockTypes defineCockTypeBySize(int cockSize) {
        if (cockSize == 0){
            return CockTypes.ZERO;
        } else if (cockSize <= 100) {
            return CockTypes.SMALL;
        } else if (cockSize <= 250) {
            return CockTypes.MEDIUM;
        } else if (cockSize <= 500) {
            return CockTypes.BIG;
        } else {
            return CockTypes.HUGE;
        }
    }

    private CockOperation zeroSizeRandom() {
        return new PlusOperation(barBotSendMessageService, userGroupData);
    }

    private CockOperation smallSizeRandom() {
        int randomNumber = randomNumberBetweenZeroAndThousand();

        if (randomNumber < 10) {
            return new FallOffOperation(barBotSendMessageService, userGroupData);
        } else if (randomNumber < 30) {
            return new FakeFallOffOperation(barBotSendMessageService, userGroupData, getRealOperation());
        } else if (randomNumber < 100) {
            return new DoubleOperation(barBotSendMessageService, userGroupData);
        } else if (randomNumber < 300) {
            return new MinusOperation(barBotSendMessageService, userGroupData);
        } else {
            return new PlusOperation(barBotSendMessageService, userGroupData);
        }
    }

    private CockOperation mediumSizeRandom() {
        int randomNumber = randomNumberBetweenZeroAndThousand();

        if (randomNumber < 20) {
            return new FallOffOperation(barBotSendMessageService, userGroupData);
        } else if (randomNumber < 40) {
            return new FakeFallOffOperation(barBotSendMessageService, userGroupData, getRealOperation());
        } else if (randomNumber < 100) {
            return new DoubleOperation(barBotSendMessageService, userGroupData);
        } else if (randomNumber < 350) {
            return new MinusOperation(barBotSendMessageService, userGroupData);
        } else {
            return new PlusOperation(barBotSendMessageService, userGroupData);
        }
    }

    private CockOperation bigSizeRandom() {
        int randomNumber = randomNumberBetweenZeroAndThousand();

        if (randomNumber < 50) {
            return new FallOffOperation(barBotSendMessageService, userGroupData);
        } else if (randomNumber < 80) {
            return new FakeFallOffOperation(barBotSendMessageService, userGroupData, getRealOperation());
        } else if (randomNumber < 120) {
            return new DoubleOperation(barBotSendMessageService, userGroupData);
        } else if (randomNumber < 450) {
            return new MinusOperation(barBotSendMessageService, userGroupData);
        } else {
            return new PlusOperation(barBotSendMessageService, userGroupData);
        }
    }

    private CockOperation hugeSizeRandom() {
        int randomNumber = randomNumberBetweenZeroAndThousand();

        if (randomNumber < 80) {
            return new FallOffOperation(barBotSendMessageService, userGroupData);
        } else if (randomNumber < 100) {
            return new FakeFallOffOperation(barBotSendMessageService, userGroupData, getRealOperation());
        } else if (randomNumber < 120) {
            return new DoubleOperation(barBotSendMessageService, userGroupData);
        } else if (randomNumber < 600) {
            return new MinusOperation(barBotSendMessageService, userGroupData);
        } else {
            return new PlusOperation(barBotSendMessageService, userGroupData);
        }
    }

    private CockOperation getRealOperation() {
        int randomNumber = randomNumberBetweenZeroAndThousand();

        if (randomNumber < 700) {
            return new PlusOperation(barBotSendMessageService, userGroupData);
        } else {
            return new DoubleOperation(barBotSendMessageService, userGroupData);
        }
    }

    private int randomNumberBetweenZeroAndThousand() {
        Random random = new Random();
        return random.nextInt(1000);
    }
}
