package booking.client.viewModel.sharedVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModelException;
import booking.client.model.ClientModelRoomInfo;
import booking.shared.objects.Booking;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class RoomInfoViewModel
{
    private Room room;
    private final ClientModelRoomInfo roomInfoModel;
    private final ViewHandler viewHandler;
    private final ObservableList<Booking> bookings;

    public RoomInfoViewModel(ViewHandler viewHandler, ClientModelRoomInfo roomInfoModel, Room room)
    {
        this.roomInfoModel = roomInfoModel;
        this.viewHandler = viewHandler;
        this.bookings = FXCollections.observableArrayList();
        this.room = room;
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
            bookings.addAll(roomInfoModel.getBookingsForRoom(room.getName(), LocalDate.MIN, LocalDate.MAX));
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
            if (roomInfoModel.isAvailable(room))
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
