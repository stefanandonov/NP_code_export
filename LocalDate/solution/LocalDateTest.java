import java.sql.Date;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.TemporalAdjusters;

/**
 * LocalDate test
 */
public class LocalDateTest {
    public static void main(String[] args) {
        System.out.println(create());
        System.out.println(parse());
        System.out.println(with().getYear());
        System.out.println(withAdjuster());
        System.out.println(plus());
        System.out.println(minus());
        System.out.println(plusPeriod());
        System.out.println(isAfter());
        System.out.println(until());
    }

    /**
     * Create a {@link LocalDate} of 2015-06-18 by using {@link LocalDate#of}
     */
    static LocalDate create() {
        return LocalDate.of(2015, 6, 18);
    }

    static LocalDate parse() {
        /**
         * Create a {@link LocalDate} of 2015-06-18 from String by using {@link LocalDate#parse}
         */
        return LocalDate.parse("2015-06-18");
    }

    static LocalDate with() {
        LocalDate ld = DateAndTimes.LD_20150618;
        /**
         * Create a {@link LocalDate} from {@link ld} with year 2015
         * by using {@link LocalDate#withYear} or {@link LocalDate#with}
         */

        return ld.withYear(2015);
    }

    static LocalDate withAdjuster() {
        LocalDate ld = DateAndTimes.LD_20150618;
        /**
         * Create a {@link LocalDate} from {@link ld} adjusted into first day of next year
         * by using {@link LocalDate#with} and {@link TemporalAdjusters#firstDayOfNextYear}
         */
        return ld.with(TemporalAdjusters.firstDayOfNextYear());
    }

    static LocalDate plus() {
        LocalDate ld = DateAndTimes.LD_20150618;

        /**
         * Create a {@link LocalDate} from {@link ld} with 10 month later
         * by using {@link LocalDate#plusMonths} or {@link LocalDate#plus}
         */
        return ld.plusMonths(10);
    }

    static LocalDate minus() {
        LocalDate ld = DateAndTimes.LD_20150618;

        /**
         * Create a {@link LocalDate} from {@link ld} with 10 days before
         * by using {@link LocalDate#minusDays} or {@link LocalDate#minus}
         */
        return ld.minusDays(10);
    }

    static LocalDate plusPeriod() {
        LocalDate ld = DateAndTimes.LD_20150618;

        /**
         * Define a {@link Period} of 1 year 2 month 3 days
         * Create a {@link LocalDate} adding the period to {@link ld} by using {@link LocalDate#plus}
         */
        Period period = Period.of(1, 2, 3);
        return ld.plus(period);
    }

    static boolean isAfter() {
        LocalDate ld = DateAndTimes.LD_20150618;
        LocalDate ld2 = DateAndTimes.LD_20150807;

        /**
         * Check whether {@link ld2} is after {@link ld} or not
         * by using {@link LocalDate#isAfter} or {@link LocalDate#isBefore}
         */
        return ld2.isAfter(ld);
    }

    static Period until() {
        LocalDate ld = DateAndTimes.LD_20150618;
        LocalDate ld2 = DateAndTimes.LD_20150807;

        /**
         * Create a period from {@link ld} till {@link ld2}
         * by using {@link LocalDate#until}
         */
        return ld.until(ld2);
    }

}

class DateAndTimes {
    public static final LocalDate LD_20150618 = LocalDate.of(2015, 6, 18);
    public static final LocalDate LD_20150807 = LocalDate.of(2015, 8, 7);
}
