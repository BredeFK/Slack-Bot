package alfred.repositories;

import alfred.models.DBquote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBquoteRepository extends JpaRepository<DBquote, Long> {
}
