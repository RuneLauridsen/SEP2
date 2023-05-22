package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.shared.objects.Booking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CoordinatorBookingMenuViewModel
{
    private ViewHandler viewHandler;
    private ObservableList<Booking> bookings;
    private ClientModel model;

    public CoordinatorBookingMenuViewModel(ViewHandler viewHandler, ClientModel model)
    {
        this.viewHandler = viewHandler;
        this.model = model;
        bookings = FXCollections.observableArrayList();
    }

    public void bookRoomAction()
    {
        viewHandler.showCoordinatorBookRoom();
    }

    public ObservableList getBookings()
    {
        bookings = FXCollections.observableArrayList();
        bookings.addAll(model.getActiveBookings());
        return bookings;
    }

    public void refreshBookings(){
        bookings.clear();
        bookings.addAll(model.getActiveBookings());
    }

    public void cancelBooking(Booking booking)
    {
        model.deleteBooking(booking);
    }

    public void ChangeToSearch(String name)
    {
        viewHandler.showRoomInfo(model.getRoom(name));
    }
}
