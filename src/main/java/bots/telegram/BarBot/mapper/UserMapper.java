package bots.telegram.BarBot.mapper;

import bots.telegram.BarBot.dto.UserDto;
import bots.telegram.BarBot.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserMapper implements BaseMapper<User, UserDto>{
    @Override
    public UserDto mapToDto(Optional<User> object) {
        return object.map(user -> new UserDto(
                user.getUserId(),
                user.getName(),
                user.getSecondName()))
                .orElse(null);
    }

    @Override
    public User mapToEntity(UserDto object) {
        return User.builder()
                .userId(object.id())
                .name(object.username())
                .secondName(object.surname())
                .build();
    }

    @Override
    public List<UserDto> mapToDtoList(List<User> objects) {
        List<UserDto> userDtoList = new ArrayList<>();

        for (User object : objects) {
            userDtoList.add(mapToDto(Optional.ofNullable(object)));
        }

        return userDtoList;
    }

    @Override
    public List<User> mapToEntityList(List<UserDto> objects) {
        List<User> userList = new ArrayList<>();

        for (UserDto object : objects) {
            userList.add(mapToEntity(object));
        }

        return userList;
    }
}
