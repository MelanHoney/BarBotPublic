package bots.telegram.BarBot.service.database;

import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.mapper.UserChatMapper;
import bots.telegram.BarBot.model.UserChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserChatService {
    private final UserChatRepository userChatRepository;
    private final UserChatMapper userChatMapper;

    public UserChatDto findById(Long userId, Long groupId) {
        return userChatMapper.mapToDto(userChatRepository
                                .findByPkUserIdAndPkGroupId(userId, groupId));
    }

    public List<UserChatDto> findTop10Cocks() {
        return userChatMapper.mapToDtoList(userChatRepository.findTop10ByOrderByCockSizeDesc());
    }

    public List<UserChatDto> findAllByChatId(Long groupId) {
        return userChatMapper.mapToDtoList(userChatRepository.findAllByPkGroupIdOrderByCockSizeDesc(groupId));
    }

    public UserChatDto save(UserChatDto userChatDto) {
        userChatRepository.save(userChatMapper.mapToEntity(userChatDto));
        return userChatDto;
    }
}
