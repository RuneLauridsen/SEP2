package booking.shared;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReadTimeNowProvider implements NowProvider
{
    @Override public LocalDate nowDate()
    {
        return LocalDate.now();
    }

    @Override public LocalTime nowTime()
    {
        return LocalTime.now();
    }

    @Override public LocalDateTime nowDateTime()
    {
        return LocalDateTime.now();
    }
}
