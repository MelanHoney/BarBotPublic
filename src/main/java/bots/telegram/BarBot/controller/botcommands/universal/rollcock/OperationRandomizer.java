package bots.telegram.BarBot.controller.botcommands.universal.rollcock;

import bots.telegram.BarBot.controller.botcommands.universal.rollcock.cocktypes.CockTypes;
import bots.telegram.BarBot.controller.botcommands.universal.rollcock.operations.*;
import bots.telegram.BarBot.dto.ChatDto;
import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.utility.CommandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Random;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class OperationRandomizer {
    private final PlusOperation plusOperation;
    private final MinusOperation minusOperation;
    private final FallOffOperation fallOffOperation;
    private final DoubleOperation doubleOperation;
    private final FakeFallOffOperation fakeFallOffOperation;
    private Message message;
    private UserChatDto dto;
    private ChatDto chatDto;

    public Stream<CommandResponse> execute(Message message, UserChatDto dto, ChatDto chatDto) {
        initFields(message, dto, chatDto);
        CockTypes cockType = defineCockTypeBySize(dto.cockSize());

        return switch (cockType) {
            case ZERO -> zeroSizeRandom();
            case SMALL -> smallSizeRandom();
            case MEDIUM -> mediumSizeRandom();
            case BIG -> bigSizeRandom();
            case HUGE -> hugeSizeRandom();
        };
    }

    private void initFields(Message message, UserChatDto dto, ChatDto chatDto) {
        this.message = message;
        this.dto = dto;
        this.chatDto = chatDto;
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

    private Stream<CommandResponse> zeroSizeRandom() {
        return plusOperation.executeOperation(message, dto, chatDto);
    }

    private Stream<CommandResponse> smallSizeRandom() {
        int randomNumber = randomNumberBetweenZeroAndThousand();

        if (randomNumber < 10) {
            return fallOffOperation.executeOperation(message, dto, chatDto);
        } else if (randomNumber < 30) {
            return fakeFallOffOperation.executeOperation(message, dto, chatDto, getRealOperation());
        } else if (randomNumber < 100) {
            return doubleOperation.executeOperation(message, dto, chatDto);
        } else if (randomNumber < 300) {
            return minusOperation.executeOperation(message, dto, chatDto);
        } else {
            return plusOperation.executeOperation(message, dto, chatDto);
        }
    }

    private Stream<CommandResponse> mediumSizeRandom() {
        int randomNumber = randomNumberBetweenZeroAndThousand();

        if (randomNumber < 20) {
            return fallOffOperation.executeOperation(message, dto, chatDto);
        } else if (randomNumber < 40) {
            return fakeFallOffOperation.executeOperation(message, dto, chatDto, getRealOperation());
        } else if (randomNumber < 100) {
            return doubleOperation.executeOperation(message, dto, chatDto);
        } else if (randomNumber < 350) {
            return minusOperation.executeOperation(message, dto, chatDto);
        } else {
            return plusOperation.executeOperation(message, dto, chatDto);
        }
    }

    private Stream<CommandResponse> bigSizeRandom() {
        int randomNumber = randomNumberBetweenZeroAndThousand();

        if (randomNumber < 50) {
            return fallOffOperation.executeOperation(message, dto, chatDto);
        } else if (randomNumber < 80) {
            return fakeFallOffOperation.executeOperation(message, dto, chatDto, getRealOperation());
        } else if (randomNumber < 120) {
            return doubleOperation.executeOperation(message, dto, chatDto);
        } else if (randomNumber < 450) {
            return minusOperation.executeOperation(message, dto, chatDto);
        } else {
            return plusOperation.executeOperation(message, dto, chatDto);
        }
    }

    private Stream<CommandResponse> hugeSizeRandom() {
        int randomNumber = randomNumberBetweenZeroAndThousand();

        if (randomNumber < 80) {
            return fallOffOperation.executeOperation(message, dto, chatDto);
        } else if (randomNumber < 100) {
            return fakeFallOffOperation.executeOperation(message, dto, chatDto, getRealOperation());
        } else if (randomNumber < 120) {
            return doubleOperation.executeOperation(message, dto, chatDto);
        } else if (randomNumber < 600) {
            return minusOperation.executeOperation(message, dto, chatDto);
        } else {
            return plusOperation.executeOperation(message, dto, chatDto);
        }
    }

    private CockOperation getRealOperation() {
        int randomNumber = randomNumberBetweenZeroAndThousand();

        if (randomNumber < 700) {
            return plusOperation;
        } else {
            return doubleOperation;
        }
    }

    private int randomNumberBetweenZeroAndThousand() {
        Random random = new Random();
        return random.nextInt(1000);
    }
}
