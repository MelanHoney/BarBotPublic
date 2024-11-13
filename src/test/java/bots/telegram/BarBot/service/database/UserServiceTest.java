package bots.telegram.BarBot.service.database;

import bots.telegram.BarBot.dto.UserDto;
import bots.telegram.BarBot.mapper.UserMapper;
import bots.telegram.BarBot.model.User;
import bots.telegram.BarBot.model.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    UserRepository userRepository;
    UserService userService;
    UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        userService = new UserService(userRepository, userMapper);
    }

    @Test
    void UserService_findById_shouldReturnNull() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Long id = 1L;

        var userDto = userService.findById(id);

        assertThat(userDto).isNull();
    }

    @Test
    void UserService_findById_shouldReturnUserDto() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        Long id = 1L;

        var userDto = userService.findById(id);

        assertThat(userDto).isNotNull().isInstanceOf(UserDto.class);
    }

    @Test
    void UserService_save_shouldReturnUserDto() {
        UserDto userDto = UserDto.builder().build();
        User userEntity = userMapper.mapToEntity(userDto);
        Mockito.when(userRepository.save(userEntity)).thenReturn(userEntity);

        var user = userService.save(userDto);

        Mockito.verify(userRepository, Mockito.times(1)).save(userEntity);
        assertThat(user).isNotNull().isInstanceOf(UserDto.class);
    }
}