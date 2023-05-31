package test;

import booking.shared.NowProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FakeNowProvider implements NowProvider
{
    // NOTE: Alle unit tests kører som om dato/tid er 2023-05-09 09:00
    private static final LocalDateTime fakeNow = LocalDateTime.of(2023, 5, 9, 9, 0, 0);

    @Override public LocalDate nowDate()
    {
        return fakeNow.toLocalDate();
    }

    @Override public LocalTime nowTime()
    {
        return fakeNow.toLocalTime();
    }

    @Override public LocalDateTime nowDateTime()
    {
        return fakeNow;
    }
}
