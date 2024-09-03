package bots.telegram.BarBot.dto;

import lombok.Builder;

@Builder
public record ChatDto(Long id,
                      String title,
                      String type,
                      int totalCockSize) {
}
