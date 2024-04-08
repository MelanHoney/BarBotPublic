package bots.telegram.BarBot.service;

import bots.telegram.BarBot.model.Group;
import bots.telegram.BarBot.model.User;
import bots.telegram.BarBot.model.UserGroup;
import bots.telegram.BarBot.model.UserGroupPK;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface DataBaseManager {
    void saveUserData(User userData);
    void saveGroupData(Group groupData);
    void saveUserGroupData(UserGroup userGroupData);
    User findUserById(Long userId);
    Group findGroupById(Long groupId);
    UserGroup findUserGroupById(UserGroupPK userGroupId);
    boolean isUserPresent(Long userId);
    boolean isGroupPresent(Long groupId);
    boolean isUserGroupPresent(UserGroupPK userGroupId);
    List<User> findAllUserDataById(List<Long> userIdList);
    List<Group> findAllGroupDataById(List<Long> groupIdList);
    List<UserGroup> findAllUserGroupDataById(List<UserGroupPK> userGroupIdList);
    List<User> findAllUserData();
    List<Group> findAllGroupData();
    List<UserGroup> findAllUserGroupData();
}
