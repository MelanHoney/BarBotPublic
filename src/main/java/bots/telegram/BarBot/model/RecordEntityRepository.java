package bots.telegram.BarBot.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordEntityRepository extends CrudRepository<RecordEntity, Long> {
}
