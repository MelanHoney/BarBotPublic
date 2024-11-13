package bots.telegram.BarBot.service.database;

import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.mapper.UserChatMapper;
import bots.telegram.BarBot.model.UserChat;
import bots.telegram.BarBot.model.UserChatPK;
import bots.telegram.BarBot.model.UserChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserChatServiceTest {

    @Mock
    UserChatRepository userChatRepository;
    UserChatService userChatService;
    UserChatMapper userChatMapper;

    @BeforeEach
    void setUp() {
        userChatMapper = new UserChatMapper();
        userChatService = new UserChatService(userChatRepository, userChatMapper);
    }

    @Test
    void UserChatService_findById_shouldReturnNull() {
        Mockito.when(userChatRepository.findByPkUserIdAndPkGroupId(1L, 1L)).thenReturn(Optional.empty());
        Long userId = 1L;
        Long groupId = 1L;

        var userChatDto = userChatService.findById(userId, groupId);

        assertThat(userChatDto).isNull();
    }

    @Test
    void UserChatService_findById_shouldReturnUserChatDto() {
        Mockito.when(userChatRepository.findByPkUserIdAndPkGroupId(1L, 1L)).thenReturn(Optional.of(
                UserChat.builder().pk(new UserChatPK(1L, 1L)).build()));
        Long userId = 1L;
        Long groupId = 1L;

        var userChatDto = userChatService.findById(userId, groupId);

        assertThat(userChatDto).isNotNull().isInstanceOf(UserChatDto.class);
    }

    @Test
    void UserChatService_findTop10Cocks_shouldReturnEmptyCollection() {
        Mockito.when(userChatRepository.findTop10ByOrderByCockSizeDesc()).thenReturn(List.of());

        List<UserChatDto> userChatDtoList = userChatService.findTop10Cocks();

        assertThat(userChatDtoList).isEmpty();
    }

    @Test
    void UserChatService_findTop10Cocks_shouldReturnUserChatDtoList() {
        Mockito.when(userChatRepository.findTop10ByOrderByCockSizeDesc()).thenReturn(List.of(
                UserChat.builder().pk(new UserChatPK(1L, 1L)).build(),
                UserChat.builder().pk(new UserChatPK(2L, 1L)).build(),
                UserChat.builder().pk(new UserChatPK(3L, 1L)).build()));

        List<UserChatDto> userChatDtoList = userChatService.findTop10Cocks();

        assertThat(userChatDtoList).isNotEmpty();
        assertThat(userChatDtoList.size()).isEqualTo(3);
        assertThat(userChatDtoList.get(0)).isInstanceOf(UserChatDto.class);
    }

    @Test
    void UserChatService_findAllByUserChatId_shouldReturnEmptyCollection() {
        Mockito.when(userChatRepository.findAllByPkGroupIdOrderByCockSizeDesc(1L)).thenReturn(List.of());

        Long userChatId = 1L;
        List<UserChatDto> userChatDtoList = userChatService.findAllByChatId(userChatId);

        assertThat(userChatDtoList).isEmpty();
    }

    @Test
    void UserChatService_findAllByUserChatId_shouldReturnUserChatDtoList() {
        Mockito.when(userChatRepository.findAllByPkGroupIdOrderByCockSizeDesc(1L)).thenReturn(List.of(
                UserChat.builder().pk(new UserChatPK(1L, 1L)).build(),
                UserChat.builder().pk(new UserChatPK(2L, 1L)).build(),
                UserChat.builder().pk(new UserChatPK(3L, 1L)).build()));

        Long chatId = 1L;
        List<UserChatDto> userChatDtoList = userChatService.findAllByChatId(chatId);

        assertThat(userChatDtoList).isNotEmpty();
        assertThat(userChatDtoList.size()).isEqualTo(3);
        assertThat(userChatDtoList.get(0)).isInstanceOf(UserChatDto.class);
        assertThat(userChatDtoList.get(0).groupId()).isEqualTo(chatId);
    }

    @Test
    void UserChatService_save_shouldReturnUserChatDto() {
        UserChatDto userChatDto = UserChatDto.builder().build();
        UserChat userChatEntity = userChatMapper.mapToEntity(userChatDto);
        Mockito.when(userChatRepository.save(userChatEntity)).thenReturn(userChatEntity);

        var userChat = userChatService.save(userChatDto);

        Mockito.verify(userChatRepository, Mockito.times(1)).save(userChatEntity);
        assertThat(userChat).isNotNull().isInstanceOf(UserChatDto.class);
    }
}