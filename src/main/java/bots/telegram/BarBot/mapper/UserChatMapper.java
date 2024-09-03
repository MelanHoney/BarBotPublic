package bots.telegram.BarBot.mapper;

import bots.telegram.BarBot.dto.UserChatDto;
import bots.telegram.BarBot.model.UserChat;
import bots.telegram.BarBot.model.UserChatPK;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserChatMapper implements BaseMapper<UserChat, UserChatDto>{
    @Override
    public UserChatDto mapToDto(Optional<UserChat> object) {
        return object.map(userGroup -> new UserChatDto(
                userGroup.getPk().getUserId(),
                userGroup.getPk().getGroupId(),
                userGroup.getCockSize(),
                userGroup.getLastCockUpdate(),
                userGroup.getNickname()))
                .orElse(null);
    }

    @Override
    public UserChat mapToEntity(UserChatDto object) {
        return new UserChat(
                new UserChatPK(object.userId(), object.groupId()),
                object.cockSize(),
                object.lastCockUpdate(),
                object.nickname()
        );
    }

    @Override
    public List<UserChatDto> mapToDtoList(List<UserChat> objects) {
        List<UserChatDto> userChatDtos = new ArrayList<>();

        for (UserChat object : objects) {
            userChatDtos.add(mapToDto(Optional.ofNullable(object)));
        }

        return userChatDtos;
    }

    @Override
    public List<UserChat> mapToEntityList(List<UserChatDto> objects) {
        ArrayList<UserChat> userChats = new ArrayList<>();

        for (UserChatDto object : objects) {
            userChats.add(mapToEntity(object));
        }

        return userChats;
    }
}
