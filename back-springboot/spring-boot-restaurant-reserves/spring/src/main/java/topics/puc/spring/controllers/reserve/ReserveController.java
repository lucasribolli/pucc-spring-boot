package topics.puc.spring.controllers.reserve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import topics.puc.spring.mail.MailHelper;
import topics.puc.spring.models.reserve.Reserve;
import topics.puc.spring.payload.request.CreateReserveRequest;
import topics.puc.spring.payload.request.UpdateReserveApprovedRequest;
import topics.puc.spring.payload.response.MessageResponse;
import topics.puc.spring.repository.ReserveRepository;
import topics.puc.spring.repository.RestaurantTableRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/reserves")
public class ReserveController {
    @Autowired
    private MailHelper mailHelper;
    private final ReserveRepository reserveRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private String tag = ReserveController.class.getSimpleName() + " /reserves";

    @Autowired
    public ReserveController(ReserveRepository reserveRepository, RestaurantTableRepository restaurantTableRepository) {
        this.reserveRepository = reserveRepository;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    /**
     * {
     * "restaurant_table_id" : 3,
     * "user_id": "lucasribolli@gmail.com",
     * "start_datetime" : "2023-08-24T08:00:00.00-0000",
     * "end_datetime": "2023-08-24T11:00:00.00-0000"
     * }
     */
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Transactional
    public ResponseEntity<MessageResponse> createReserve(@RequestBody CreateReserveRequest request) {
        try {
            Reserve reserve = new Reserve();
            reserve.setRestaurant_table_id(request.getRestaurant_table_id());
            reserve.setStart_datetime(request.getStart_datetime());
            reserve.setEnd_datetime(request.getEnd_datetime());
            reserve.setUser_id(request.getUser_id());

            try {
                ReserveRequestValidator validator = new ReserveRequestValidator(reserve, reserveRepository, restaurantTableRepository);
                validator.validate();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
            }

            Reserve createdReserve = reserveRepository.save(reserve);

            mailHelper.sendEmail(
                    reserve.getUser_id(),
                    "Reserve created",
                    "Table: " + reserve.getRestaurant_table_id() +
                            "\nFrom: " + reserve.getStart_datetime() +
                            "\nTo: " + reserve.getEnd_datetime()
            );

            return ResponseEntity.ok(new MessageResponse(
                    createdReserve.getId().toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping(
            name = "/all",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Transactional
    public ResponseEntity<List<Reserve>> getAll() {
        System.out.println(tag + "getReserveById...");
        List<Reserve> reserveData = reserveRepository.findAll();

        reserveData.sort(Comparator.comparingLong(Reserve::getId));

        return ResponseEntity.ok(reserveData);
    }

    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Transactional
    public ResponseEntity<MessageResponse> updateApproved(
            @RequestBody UpdateReserveApprovedRequest request
    ) {
        try {
            System.out.println(tag + "updateApproved...");
            Optional<Reserve> optionalReserve = reserveRepository.findById(request.getReserve_id());
            if (optionalReserve.isPresent()) {
                Reserve reserve = optionalReserve.get();
                reserve.setApproved(request.getApproved());
                reserveRepository.save(reserve);
                return ResponseEntity.ok(new MessageResponse("Approved status updated"));
            }
            return ResponseEntity.badRequest().body(new MessageResponse("Reserve not found"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<MessageResponse> deleteReserveById(
            @RequestParam
            Long id
    ) {
        try {
            System.out.println(tag + "deleteReserveById...");
            Optional<Reserve> optionalReserve = reserveRepository.findById(id);
            if (optionalReserve.isPresent()) {
                Reserve reserve = optionalReserve.get();
                reserveRepository.deleteById(id);
                return ResponseEntity.ok(new MessageResponse("Resource deleted successfully " + id));
            }
            return ResponseEntity.badRequest().body(new MessageResponse("Reserve not found"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }

}