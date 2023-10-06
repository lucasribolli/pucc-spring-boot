package topics.puc.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import topics.puc.spring.models.restaurant_table.RestaurantTable;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
}
