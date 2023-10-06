package topics.puc.spring.repository;

import topics.puc.spring.models.reserve.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
}
