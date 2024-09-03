package bots.telegram.BarBot.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Long> {

    @Query( value = "SELECT group_id, title, type, total_cock_size FROM group_data WHERE type != 'private' ORDER BY total_cock_size DESC LIMIT 10",
            nativeQuery = true)
    List<Chat> findTop10ByOrderByTotalCockSizeDesc();
}
