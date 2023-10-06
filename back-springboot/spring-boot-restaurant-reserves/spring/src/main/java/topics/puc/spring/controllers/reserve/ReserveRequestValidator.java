package topics.puc.spring.controllers.reserve;

import org.springframework.http.ResponseEntity;
import topics.puc.spring.Constants;
import topics.puc.spring.models.reserve.Reserve;
import topics.puc.spring.models.restaurant_table.RestaurantTable;
import topics.puc.spring.payload.response.MessageResponse;
import topics.puc.spring.repository.ReserveRepository;
import topics.puc.spring.repository.RestaurantTableRepository;

import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ReserveRequestValidator {
    private final Reserve reserve;
    private final ReserveRepository reserveRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    public ReserveRequestValidator(Reserve reserve, ReserveRepository reserveRepository, RestaurantTableRepository restaurantTableRepository) {
        this.reserve = reserve;
        this.reserveRepository = reserveRepository;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    public void validate() throws IllegalArgumentException {
        if (!doesTableExists()) {
            throw new IllegalArgumentException("Restaurant table does not exists.");
        }
        if (!isTimePeriodOnWorkTime()) {
            throw new IllegalArgumentException("Reserve time range is out of restaurant work time.");
        }
        if (!isDurationLessThanMaxTime()) {
            throw new IllegalArgumentException("Reserve duration is too long.");
        }
        if (!noReserveAtSameTableAndTime()) {
            throw new IllegalArgumentException("Reserve are coinciding with another on the same table.");
        }
    }

    private boolean doesTableExists() {
        Long tableRequestId = reserve.getRestaurant_table_id();
        Optional<RestaurantTable> table = restaurantTableRepository.findById(tableRequestId);
        return table.isPresent();
    }

    private boolean noReserveAtSameTableAndTime() {
        List<Reserve> all = reserveRepository.findAll();
        List<Reserve> atSameRestaurantTable = all.stream().filter(r ->
                Objects.equals(r.getRestaurant_table_id(), reserve.getRestaurant_table_id())).toList();

        List<DateRange> occupiedRanges =
                atSameRestaurantTable.stream().map(r -> new DateRange(r.getStart_datetime(), r.getEnd_datetime())).toList();
        DateRange rangeToVerify = new DateRange(reserve.getStart_datetime(), reserve.getEnd_datetime());

        for (DateRange occupiedRange : occupiedRanges) {
            if (occupiedRange.isDateRangeWithinOuterRange(rangeToVerify, occupiedRange)) {
                System.out.println("rangeToVerify is on occupiedRange");
                return false;
            }
            if (occupiedRange.isDateRangeWithinOuterRange(occupiedRange, rangeToVerify)) {
                System.out.println("occupiedRange is on rangeToVerify");
                return false;
            }
        }
        return true;
    }

    private boolean isTimePeriodOnWorkTime() {
        LocalTime start = dateToUTCLocalTime(reserve.getStart_datetime());
        LocalTime end = dateToUTCLocalTime(reserve.getEnd_datetime());
        return !start.isBefore(Constants.RESTAURANT_OPEN_TIME)
                && !end.isAfter(Constants.RESTAURANT_CLOSE_TIME);
    }

    private boolean isDurationLessThanMaxTime() {
        long differenceInSeconds =
                ChronoUnit.SECONDS.between(reserve.getStart_datetime().toInstant(), reserve.getEnd_datetime().toInstant());

        return differenceInSeconds <= Constants.MAX_RESERVE_DURATION_IN_SECONDS;
    }

    private LocalTime dateToUTCLocalTime(Date date) {
        // Convert java.util.Date to Instant
        Instant instant = date.toInstant();

        // Convert Instant to LocalTime (assuming you want to use the system's default time zone)
        return instant.atZone(java.time.ZoneOffset.UTC).toLocalTime();
    }

    record DateRange(Date startDate, Date endDate) {
        /**
         * innerRange: DateRange("08:00", "12:00");
         * outerRange: DateRange("06:00", "18:00");
         * returns true
         */
        public boolean isDateRangeWithinOuterRange(DateRange innerRange, DateRange outerRange) {
            return !innerRange.startDate.before(outerRange.startDate) && !innerRange.endDate.after(outerRange.endDate);
        }
    }
}
