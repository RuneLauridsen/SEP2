package booking;

import booking.core.Booking;
import booking.core.BookingInterval;
import booking.core.BookingSystem;
import booking.core.User;
import booking.core.UserType;
import booking.core.Room;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestData
{
    public BookingSystem getTestData()
    {
        BookingSystem model = new BookingSystem();

        /*
        User brugerA = new User(1, UserType.USER_TYPE_STUDENT);
        User brugerB = new User(1, UserType.USER_TYPE_STUDENT);
        User brugerC = new User(1, UserType.USER_TYPE_STUDENT);

        Room roomA = new Room("C02.01");
        Room roomB = new Room("C02.02");
        Room roomC = new Room("C02.03");
        Room roomD = new Room("C02.04");
        Room roomE = new Room("C02.05");

        model.addRoom(roomA);
        model.addRoom(roomB);
        model.addRoom(roomC);
        model.addRoom(roomD);
        model.addRoom(roomE);

        // Room A 10:00 - 16:00
        model.addBooking(new Booking(
            new BookingInterval(
                LocalDate.now(),
                LocalTime.of(10, 0, 0),
                LocalTime.of(16, 0, 0)),
            brugerA,
            roomA));

        // Room B 14:00 - 18:00
        model.addBooking(new Booking(
            new BookingInterval(
                LocalDate.now(),
                LocalTime.of(14, 0, 0),
                LocalTime.of(18, 0, 0)),
            brugerB,
            roomB));
        */
        return model;
    }
}
