package topics.puc.spring.payload.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class CreateReserveRequest {
    @NotBlank
    private Long restaurant_table_id;
    @NotBlank
    private String user_id;
    @NotBlank
    private Date start_datetime;
    @NotBlank
    private Date end_datetime;

    @NotBlank
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
}
