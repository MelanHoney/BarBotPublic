package bots.telegram.BarBot.mapper;

import bots.telegram.BarBot.dto.UserDto;
import bots.telegram.BarBot.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void UserMapper_mapToDto_shouldReturnNull() {
        Optional<User> user = Optional.empty();

        var userDto = userMapper.mapToDto(user);

        assertThat(userDto).isNull();
    }

    @Test
    void UserMapper_mapToDto_shouldReturnDto() {
        Optional<User> user = Optional.ofNullable(
                User.builder().userId(1L).name("name").secondName("second name").build());

        var userDto = userMapper.mapToDto(user);

        assertThat(userDto).isInstanceOf(UserDto.class);
    }


    @Test
    void UserMapper_mapToEntity_shouldReturnEntities() {
        UserDto userDto = UserDto.builder().id(1L).username("name").surname("second name").build();

        User user = userMapper.mapToEntity(userDto);

        assertThat(user).isInstanceOf(User.class);
    }

    @Test
    void UserMapper_mapToDtoList_shouldReturnNullList() {
        List<User> users = List.of();

        List<UserDto> userDtoList = userMapper.mapToDtoList(users);

        assertThat(userDtoList).isEmpty();
    }

    @Test
    void UserMapper_mapToDtoList_shouldReturnDtoList() {
        User user = User.builder().userId(1L).name("name").secondName("second name").build();
        List<User> users = List.of(user, user);

        List<UserDto> userDtoList = userMapper.mapToDtoList(users);

        assertThat(userDtoList).isNotEmpty();
    }


    @Test
    void UserMapper_mapToEntityList_shouldReturnEntities() {
        UserDto userDto = UserDto.builder().id(1L).username("name").surname("second name").build();
        List<UserDto> userDtoList = List.of(userDto, userDto);

        List<User> users = userMapper.mapToEntityList(userDtoList);

        assertThat(users).isNotEmpty();
    }
}