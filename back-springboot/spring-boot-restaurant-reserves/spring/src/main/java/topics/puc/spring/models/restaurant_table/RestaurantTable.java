package topics.puc.spring.models.restaurant_table;

import jakarta.persistence.*;
import topics.puc.spring.models.reserve.Reserve;

@Entity
@Table(name = "restaurant_tables")
public class RestaurantTable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "capacity")
    private Long capacity;

    public RestaurantTable() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }
}
