package booking.client.view.roomInfo;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelException;
import booking.shared.objects.Booking;
import booking.shared.objects.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class RoomInfoViewModel
{
    private final Room room;
    private final ClientModel model;
    private final ViewHandler viewHandler;
    private ObservableList<Booking> bookings;

    public RoomInfoViewModel(ViewHandler viewHandler, ClientModel model, Room room)
    {
        this.room = room;
        this.model = model;
        this.viewHandler = viewHandler;
        this.bookings = FXCollections.observableArrayList();

    }

    public Room getRoom()
    {
        return room;
    }

    public ObservableList<Booking> getBookings()
    {
        bookings.clear();

        try
        {
            bookings.addAll(model.getBookingsForRoom(room.getName(), LocalDate.MIN, LocalDate.MAX));
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }

        return bookings;
    }

    public String isAvailable()
    {
        try
        {
            if (model.isAvailable(room))
            {
                return "Available";
            }
            else
            {
                return "Occupied";
            }
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
            return "";
        }
    }
}
