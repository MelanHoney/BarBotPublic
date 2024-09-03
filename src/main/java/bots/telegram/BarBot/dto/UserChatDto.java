package bots.telegram.BarBot.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserChatDto(Long userId,
                          Long groupId,
                          int cockSize,
                          LocalDate lastCockUpdate,
                          String nickname) {
}
