package bots.telegram.BarBot.mapper;

import bots.telegram.BarBot.dto.ChatDto;
import bots.telegram.BarBot.model.Chat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ChatMapperTest {

    ChatMapper chatMapper;

    @BeforeEach
    void setUp() {
        chatMapper = new ChatMapper();
    }

    @Test
    void ChatMapper_mapToDto_shouldReturnNull() {
        Optional<Chat> chat = Optional.empty();

        var chatDto = chatMapper.mapToDto(chat);

        assertThat(chatDto).isNull();
    }

    @Test
    void ChatMapper_mapToDto_shouldReturnDtos() {
        Optional<Chat> privateChat = Optional.ofNullable(
                Chat.builder().chatId(1L).type("private").title(null).totalCockSize(123).build());
        Optional<Chat> groupChat = Optional.ofNullable(
                Chat.builder().chatId(2L).type("group").title("test").totalCockSize(123).build());

        var privateChatDto = chatMapper.mapToDto(privateChat);
        var groupChatDto = chatMapper.mapToDto(groupChat);

        assertThat(groupChatDto).isInstanceOf(ChatDto.class);
        assertThat(privateChatDto).isInstanceOf(ChatDto.class);
    }


    @Test
    void ChatMapper_mapToEntity_shouldReturnEntities() {
        ChatDto privateChatDto = ChatDto.builder().id(1L).type("private").title(null).totalCockSize(123).build();
        ChatDto groupChatDto = ChatDto.builder().id(2L).type("group").title("test").totalCockSize(123).build();

        Chat privateChat = chatMapper.mapToEntity(privateChatDto);
        Chat groupChat = chatMapper.mapToEntity(groupChatDto);

        assertThat(privateChat).isInstanceOf(Chat.class);
        assertThat(groupChat).isInstanceOf(Chat.class);
    }

    @Test
    void ChatMapper_mapToDtoList_shouldReturnNullList() {
        List<Chat> chats = List.of();

        List<ChatDto> chatDtoList = chatMapper.mapToDtoList(chats);

        assertThat(chatDtoList).isEmpty();
    }

    @Test
    void ChatMapper_mapToDtoList_shouldReturnDtoList() {
        Chat privateChat = Chat.builder().chatId(1L).type("private").title(null).totalCockSize(123).build();
        Chat groupChat = Chat.builder().chatId(2L).type("group").title("test").totalCockSize(123).build();
        List<Chat> chats = List.of(privateChat, groupChat);

        List<ChatDto> chatDtoList = chatMapper.mapToDtoList(chats);

        assertThat(chatDtoList).isNotEmpty();
    }


    @Test
    void ChatMapper_mapToEntityList_shouldReturnEntities() {
        ChatDto privateChatDto = ChatDto.builder().id(1L).type("private").title(null).totalCockSize(123).build();
        ChatDto groupChatDto = ChatDto.builder().id(2L).type("group").title("test").totalCockSize(123).build();
        List<ChatDto> chatDtoList = List.of(privateChatDto, groupChatDto);

        List<Chat> chats = chatMapper.mapToEntityList(chatDtoList);

        assertThat(chats).isNotEmpty();
    }
}