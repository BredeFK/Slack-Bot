package alfred.repositories;

import alfred.models.DailyQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyQuoteRepository extends JpaRepository<DailyQuote, Long> {
}
