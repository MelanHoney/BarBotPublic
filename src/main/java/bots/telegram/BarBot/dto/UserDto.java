package bots.telegram.BarBot.dto;

import lombok.Builder;

@Builder
public record UserDto(Long id,
                      String username,
                      String surname) {
}
