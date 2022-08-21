import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

/**
 * LocalTime API tests
 */
public class LocalTimeTest {
    public static void main(String[] args) {
        System.out.println(localTimeOfHourToMinute());
        System.out.println(localTimeOfHourToNanoSec());
        System.out.println(localTimeParse());
        System.out.println(localTimeWith());
        System.out.println(localTimePlus());
        System.out.println(localTimeMinus());
        System.out.println(localTimeMinusDuration());
        System.out.println(localDateIsBefore());
        System.out.println(localTimeTruncatedTo());
    }

    static LocalTime localTimeOfHourToMinute() {
        /**
         * Create a {@link LocalTime} of 23:07 by using {@link LocalTime#of}
         */
        return null;
    }

    static LocalTime localTimeOfHourToNanoSec() {
        /**
         * Create a {@link LocalTime} of 23:07:03.1 by using {@link LocalTime#of}
         */
        return null;
    }

    static LocalTime localTimeParse() {
        /**
         * Create a {@link LocalTime} of 23:07:03.1 from String by using {@link LocalTime#parse}
         */
        return null;
    }

    static LocalTime localTimeWith() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Create a {@link LocalTime} from {@link lt} with hour 21
         * by using {@link LocalTime#withHour} or {@link LocalTime#with}
         */
        return null;
    }

    static LocalTime localTimePlus() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Create a {@link LocalTime} from {@link lt} with 30 minutes later
         * by using {@link LocalTime#plusMinutes} or {@link LocalTime#plus}
         */
        return null;
    }

    static LocalTime localTimeMinus() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Create a {@link LocalTime} from {@link lt} with 3 hours before
         * by using {@link LocalTime#minusHours} or {@link LocalTime#minus}
         */
        return null;
    }


    static LocalTime localTimeMinusDuration() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Define a {@link Duration} of 3 hours 30 minutes and 20.2 seconds
         * Create a {@link LocalTime} subtracting the duration from {@link lt} by using {@link LocalTime#minus}
         */
        return null;
    }

    static boolean localDateIsBefore() {
        LocalTime lt = DateAndTimes.LT_23073050;
        LocalTime lt2 = DateAndTimes.LT_12100000;
        /**
         * Check whether {@link lt2} is before {@link lt} or not
         * by using {@link LocalTime#isAfter} or {@link LocalTime#isBefore}
         */
        return false;
    }

    static LocalTime localTimeTruncatedTo() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Create a {@link LocalTime} from {@link lt} truncated to minutes by using {@link LocalTime#truncatedTo}
         */
        return null;
    }

    static class DateAndTimes {
        public static final LocalTime LT_23073050 = LocalTime.of(23, 7, 30, 500000000);
        public static final LocalTime LT_12100000 = LocalTime.of(12, 10);
    }

}
