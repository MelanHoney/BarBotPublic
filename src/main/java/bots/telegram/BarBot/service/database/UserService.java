package bots.telegram.BarBot.service.database;

import bots.telegram.BarBot.dto.UserDto;
import bots.telegram.BarBot.mapper.UserMapper;
import bots.telegram.BarBot.model.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto findById(Long id) {
        return userMapper.mapToDto(userRepository.findById(id));
    }

    public UserDto save(UserDto user) {
        var userEntity = userMapper.mapToEntity(user);
        userRepository.save(userEntity);
        return user;
    }
}
