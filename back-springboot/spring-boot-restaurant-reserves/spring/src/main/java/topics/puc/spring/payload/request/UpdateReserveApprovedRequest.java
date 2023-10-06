package topics.puc.spring.payload.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public class UpdateReserveApprovedRequest {
    @NotBlank
    private Long reserve_id;

    private Boolean approved;

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Long getReserve_id() {
        return reserve_id;
    }

    public void setReserve_id(Long reserve_id) {
        this.reserve_id = reserve_id;
    }
}
