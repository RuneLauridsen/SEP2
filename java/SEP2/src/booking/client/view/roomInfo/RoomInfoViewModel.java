package booking.client.view.roomInfo;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.shared.objects.Booking;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class RoomInfoViewModel
{
    private Room room;
    private ClientModel model;
    private ObservableList<BookingInterval> bookings;

    public RoomInfoViewModel(ViewHandler viewHandler, ClientModel model, Room room)
    {
        this.room = room;
        this.model = model;
        bookings = FXCollections.observableArrayList();

    }

    public Room getRoom()
    {
        return room;
    }

    public ObservableList<BookingInterval> getBookings()
    {
        bookings.clear();

        // TODO(rune): Hvorfor er observeable list med BookingInterval?

        List<Booking> bookingsFromModel = model.getBookingsForRoom(room.getName(), LocalDate.MIN, LocalDate.MAX);
        for (Booking booking : bookingsFromModel)
        {
            bookings.add(booking.getInterval());
        }

        return bookings;
    }

    public String isAvailable()
    {
        if (model.isAvailable(room))
            return "Available";
        else
            return "Occupied";
    }
}
