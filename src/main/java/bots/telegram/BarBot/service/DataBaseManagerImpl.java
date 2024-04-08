package bots.telegram.BarBot.service;

import bots.telegram.BarBot.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataBaseManagerImpl implements DataBaseManager {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;

    public DataBaseManagerImpl() {

    }

    @Override
    public void saveUserData(User userData) {
        userRepository.save(userData);
    }

    @Override
    public void saveGroupData(Group groupData) {
        groupRepository.save(groupData);
    }

    @Override
    public void saveUserGroupData(UserGroup userGroupData) {
        userGroupRepository.save(userGroupData);
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId).get();
    }

    @Override
    public UserGroup findUserGroupById(UserGroupPK userGroupId) {
        return userGroupRepository.findById(userGroupId).get();
    }

    @Override
    public boolean isUserPresent(Long userId) {
        return userRepository.findById(userId).isPresent();
    }

    @Override
    public boolean isGroupPresent(Long groupId) {
        return groupRepository.findById(groupId).isPresent();
    }

    @Override
    public boolean isUserGroupPresent(UserGroupPK userGroupId) {
        return userGroupRepository.findById(userGroupId).isPresent();
    }

    @Override
    public List<User> findAllUserDataById(List<Long> userIdList) {
        return (List<User>) userRepository.findAllById(userIdList);
    }

    @Override
    public List<Group> findAllGroupDataById(List<Long> groupIdList) {
        return (List<Group>) groupRepository.findAllById(groupIdList);
    }

    @Override
    public List<UserGroup> findAllUserGroupDataById(List<UserGroupPK> userGroupIdList) {
        return (List<UserGroup>) userGroupRepository.findAllById(userGroupIdList);
    }

    @Override
    public List<User> findAllUserData() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public List<Group> findAllGroupData() {
        return (List<Group>) groupRepository.findAll();
    }

    @Override
    public List<UserGroup> findAllUserGroupData() {
        return (List<UserGroup>) userGroupRepository.findAll();
    }
}
