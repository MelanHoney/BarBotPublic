package bots.telegram.BarBot.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChatRepository extends CrudRepository<UserChat, UserChatPK> {

    List<UserChat> findTop10ByOrderByCockSizeDesc();

    List<UserChat> findAllByPkGroupIdOrderByCockSizeDesc(Long chatId);

    Optional<UserChat> findByPkUserIdAndPkGroupId(Long userId, Long chatId);
}
