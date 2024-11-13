package bots.telegram.BarBot.service.database;

import bots.telegram.BarBot.dto.ChatDto;
import bots.telegram.BarBot.mapper.ChatMapper;
import bots.telegram.BarBot.model.Chat;
import bots.telegram.BarBot.model.ChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    ChatRepository chatRepository;
    ChatService chatService;
    ChatMapper chatMapper;

    @BeforeEach
    void setUp() {
        chatMapper = new ChatMapper();
        chatService = new ChatService(chatRepository, chatMapper);
    }

    @Test
    void ChatService_findById_shouldReturnNull() {
        Mockito.when(chatRepository.findById(1L)).thenReturn(Optional.empty());
        Long id = 1L;

        var chatDto = chatService.findById(id);

        assertThat(chatDto).isNull();
    }

    @Test
    void ChatService_findById_shouldReturnChatDto() {
        Mockito.when(chatRepository.findById(1L)).thenReturn(Optional.of(new Chat()));
        Long id = 1L;

        var chatDto = chatService.findById(id);

        assertThat(chatDto).isNotNull().isInstanceOf(ChatDto.class);
    }

    @Test
    void ChatService_findTopTenByTotalCock_shouldReturnEmptyCollection() {
        Mockito.when(chatRepository.findTop10ByOrderByTotalCockSizeDesc()).thenReturn(List.of());

        List<ChatDto> chatDtoList = chatService.findTopTenByTotalCock();

        assertThat(chatDtoList).isEmpty();
    }

    @Test
    void ChatService_findTopTenByTotalCock_shouldReturnChatDtoList() {
        Mockito.when(chatRepository.findTop10ByOrderByTotalCockSizeDesc()).thenReturn(List.of(new Chat(), new Chat(), new Chat()));

        List<ChatDto> chatDtoList = chatService.findTopTenByTotalCock();

        assertThat(chatDtoList).isNotEmpty();
        assertThat(chatDtoList.size()).isEqualTo(3);
        assertThat(chatDtoList.get(0)).isInstanceOf(ChatDto.class);
    }

    @Test
    void ChatService_save_shouldReturnChatDto() {
        ChatDto chatDto = ChatDto.builder().build();
        Chat chatEntity = chatMapper.mapToEntity(chatDto);
        Mockito.when(chatRepository.save(chatEntity)).thenReturn(chatEntity);

        var chat = chatService.save(chatDto);

        Mockito.verify(chatRepository, Mockito.times(1)).save(chatEntity);
        assertThat(chat).isNotNull().isInstanceOf(ChatDto.class);
    }
}