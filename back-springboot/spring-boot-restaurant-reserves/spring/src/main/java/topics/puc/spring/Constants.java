package topics.puc.spring;

import java.time.LocalTime;

public class Constants {

    private Constants() {}

    public static LocalTime RESTAURANT_OPEN_TIME = LocalTime.of(8, 0);
    public static LocalTime RESTAURANT_CLOSE_TIME = LocalTime.of(17, 0);
    public static long MAX_RESERVE_DURATION_IN_SECONDS = 4 * (60 * 60);
}
