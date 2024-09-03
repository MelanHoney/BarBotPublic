package bots.telegram.BarBot.mapper;

import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.model.UserChat;
import bots.telegram.BarBot.model.UserChatPK;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserChatMapperTest {
    UserChatMapper userChatMapper;

    @BeforeEach
    void setUp() {
        userChatMapper = new UserChatMapper();
    }

    @Test
    void UserChatMapper_mapToDto_shouldReturnNull() {
        Optional<UserChat> chat = Optional.empty();

        var chatDto = userChatMapper.mapToDto(chat);

        assertThat(chatDto).isNull();
    }

    @Test
    void UserChatMapper_mapToDto_shouldReturnDto() {
        Optional<UserChat> userChat = Optional.ofNullable(
                UserChat.builder().
                        pk(new UserChatPK(1L, 1L))
                        .cockSize(1)
                        .lastCockUpdate(LocalDate.EPOCH)
                        .nickname("test")
                        .build());

        var userChatDto = userChatMapper.mapToDto(userChat);

        assertThat(userChatDto).isInstanceOf(UserChatDto.class);
    }


    @Test
    void UserChatMapper_mapToEntity_shouldReturnEntities() {
        UserChatDto userChatDto = UserChatDto.builder()
                .userId(1L)
                .groupId(1L)
                .cockSize(1)
                .lastCockUpdate(LocalDate.EPOCH)
                .nickname("test")
                .build();

        UserChat userChat = userChatMapper.mapToEntity(userChatDto);

        assertThat(userChat).isInstanceOf(UserChat.class);
    }

    @Test
    void UserChatMapper_mapToDtoList_shouldReturnNullList() {
        List<UserChat> chats = List.of();

        List<UserChatDto> chatDtoList = userChatMapper.mapToDtoList(chats);

        assertThat(chatDtoList).isEmpty();
    }

    @Test
    void UserChatMapper_mapToDtoList_shouldReturnDtoList() {
        UserChat userChat = UserChat.builder().
                        pk(new UserChatPK(1L, 1L))
                        .cockSize(1)
                        .lastCockUpdate(LocalDate.EPOCH)
                        .nickname("test")
                        .build();
        List<UserChat> chats = List.of(userChat, userChat);

        List<UserChatDto> chatDtoList = userChatMapper.mapToDtoList(chats);

        assertThat(chatDtoList).isNotEmpty();
    }


    @Test
    void UserChatMapper_mapToEntityList_shouldReturnEntities() {
        UserChatDto userChatDto = UserChatDto.builder()
                .userId(1L)
                .groupId(1L)
                .cockSize(1)
                .lastCockUpdate(LocalDate.EPOCH)
                .nickname("test")
                .build();
        List<UserChatDto> chatDtoList = List.of(userChatDto, userChatDto);

        List<UserChat> chats = userChatMapper.mapToEntityList(chatDtoList);

        assertThat(chats).isNotEmpty();
    }
}