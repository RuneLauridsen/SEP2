package booking.shared;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// NOTE(rune): Interface så vi kan unit test uden at dags dato har indflydelse
// på testens resultat.
public interface NowProvider
{
    public LocalDate nowDate();
    public LocalTime nowTime();
    public LocalDateTime nowDateTime();
}
