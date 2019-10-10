package alfred.repositories;

import alfred.models.ipify.IPInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPInfoRepository extends JpaRepository<IPInfo, String> {
}
