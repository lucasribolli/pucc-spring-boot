package topics.puc.spring.models.reserve;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(
        name = "reserves"
)
public class Reserve {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    // TODO Create relation OneOne to restaurant table
    @Column(name = "restaurant_table_id")
    private Long restaurant_table_id;

    @Column(name = "user_id")
    private String user_id;

    @Column(name = "start_datetime")
    private Date start_datetime;

    @Column(name = "end_datetime")
    private Date end_datetime;

    @Column(name = "approved")
    private Boolean approved;

    public Long getId() {
        return id;
    }

    public Long getRestaurant_table_id() {
        return restaurant_table_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public Date getStart_datetime() {
        return start_datetime;
    }

    public Date getEnd_datetime() {
        return end_datetime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRestaurant_table_id(Long restaurant_table_id) {
        this.restaurant_table_id = restaurant_table_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setStart_datetime(Date start_datetime) {
        this.start_datetime = start_datetime;
    }

    public void setEnd_datetime(Date end_datetime) {
        this.end_datetime = end_datetime;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
