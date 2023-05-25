package booking.client.viewModel.userGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelActiveUser;
import booking.client.model.ClientModelException;
import booking.client.model.ClientModelUserBooking;
import booking.shared.CreateBookingParameters;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;
import booking.shared.objects.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class UserBookRoomViewModel
{
    private final ObservableList<String> timeIntervals;
    private final ObservableList<Character> buildings;
    private final ObservableList<Integer> floors;

    private final ObjectProperty<LocalDate> selectedDate;
    private final ObjectProperty<String> selectedFromTime;
    private final ObjectProperty<String> selectedToTime;
    private final ObjectProperty<Character> selectedBuilding;
    private final ObjectProperty<Integer> selectedFloor;
    private final ObjectProperty<String> selectedMaxCap;
    private final ObjectProperty<String> selectedMinCap;

    private final ViewHandler viewHandler;
    private final ClientModelUserBooking bookingModel;
    private final UserViewModelState sharedState;

    private final ObservableList<Room> roomList;

    public UserBookRoomViewModel(ViewHandler viewHandler, ClientModelUserBooking bookingModel, UserViewModelState sharedState)
    {
        this.viewHandler = viewHandler;
        this.bookingModel = bookingModel;
        this.sharedState = sharedState;

        timeIntervals = FXCollections.observableArrayList(
            "7:00", "7:15", "7:30", "7:45", "8:00", "8:15", "8:30", "8:45",
            "9:00", "9:15", "9:30", "9:45", "10:00", "10:15", "10:30", "10:45",
            "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45",
            "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45",
            "15:00", "15:15", "15:30", "15:45", "16:00");

        buildings = FXCollections.observableArrayList(null, 'A', 'B', 'C');

        User user = sharedState.getUser();

        if (user.getType().getId() == 1 || user.getType().getId() == 4)
            floors = FXCollections.observableArrayList(null, 1, 2, 3, 4, 5, 6);
        else
            floors = FXCollections.observableArrayList(null, 1, 2, 3, 4, 5);

        selectedDate = new SimpleObjectProperty<>();
        selectedFromTime = new SimpleObjectProperty<>();
        selectedToTime = new SimpleObjectProperty<>();
        selectedBuilding = new SimpleObjectProperty<>();
        selectedFloor = new SimpleObjectProperty<>();
        selectedMaxCap = new SimpleObjectProperty<>();
        selectedMinCap = new SimpleObjectProperty<>();

        roomList = FXCollections.observableArrayList();
    }

    public ObservableList<String> getTimeIntervals()
    {
        return timeIntervals;
    }

    public ObservableList<Character> getBuildings()
    {
        return buildings;
    }

    public ObservableList<Integer> getFloors()
    {
        return floors;
    }

    public ObjectProperty<LocalDate> selectedDateProperty()
    {
        return selectedDate;
    }

    public ObjectProperty<String> selectedFromTimeProperty()
    {
        return selectedFromTime;
    }

    public ObjectProperty<String> selectedToTimeProperty()
    {
        return selectedToTime;
    }

    public ObjectProperty<Character> selectedBuildingProperty()
    {
        return selectedBuilding;
    }

    public ObjectProperty<Integer> selectedFloorProperty()
    {
        return selectedFloor;
    }

    public ObservableList<Room> getRoomList()
    {
        return roomList;
    }

    public void showAvailableRooms()
    {
        try
        {
            // TODO(rune): Min/max cap

            String startTimeString = selectedFromTime.get();
            String endTimeString = selectedToTime.get();
            Character building = selectedBuilding.get();
            Integer floor = selectedFloor.get();
            LocalDate date = selectedDate.get();

            // TODO(rune): timeIntervals list kunne evt. være med <LocalTime> i stedet,
            // så vi slipper for at parse her.
            LocalTime startTime = parseLocalDateTime(startTimeString);
            LocalTime endTime = parseLocalDateTime(endTimeString);

            if (startTime.isAfter(endTime)){
                viewHandler.showErrorDialog("Start time of booking must be before end time of booking");
            }
            else {
                GetAvailableRoomsParameters parameters = new GetAvailableRoomsParameters(
                        date, startTime, endTime
                );

                parameters.setBuilding(building);
                parameters.setFloor(floor);
                if (selectedMinCap.get() != null)
                    parameters.setMinCapacity(Integer.parseInt(selectedMinCap.get()));
                if (selectedMaxCap.get() != null)
                    parameters.setMaxCapacity(Integer.parseInt(selectedMaxCap.get()) );


                List<Room> roomsFromDatabase = bookingModel.getAvailableRooms(parameters);
                roomList.clear();
                roomList.addAll(roomsFromDatabase);

            }


        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
        catch (NumberFormatException e)
        {
            viewHandler.showErrorDialog("Max- and min capacity must be a number");
        }
        catch (NullPointerException e){
            viewHandler.showErrorDialog("Date and time interval must not be empty");
        }
    }

    public void bookRoom(Room room)
    { //TODO billede i rapport skal opdateres hvis metoden ændres
        LocalTime startTime = parseLocalDateTime(selectedFromTime.get());
        LocalTime endTime = parseLocalDateTime(selectedToTime.get());
        BookingInterval requestedInterval = new BookingInterval(selectedDate.get(), startTime, endTime);
        CreateBookingParameters parameters = new CreateBookingParameters(
            room,
            requestedInterval,
            true,    // overlap tilladt,
            null);   // ikke til hold/klassen
        try
        {
            bookingModel.createBooking(parameters);
            viewHandler.showInfoDialog("Lokale " + room + " er booket til " + requestedInterval);
            sharedState.refreshActiveBookings();

            // NOTE(rune): Genindlæs så lokalet der lige er bliver booket forsvinder fra listen.
            showAvailableRooms();
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }

    private static LocalTime parseLocalDateTime(String s)
    {
        // TODO(rune): Argument validation

        String[] parts = s.split(":");
        int minute = Integer.parseInt(parts[1]);
        int hour = Integer.parseInt(parts[0]);

        return LocalTime.of(hour, minute);
    }

    public void ChangeToSearch(String roomName)
    {
        viewHandler.showRoomInfo(roomName);
    }

    public Property<String> selectedMaxCap() {
        return selectedMaxCap;
    }
    public Property<String> selectedMinCap() {
        return selectedMinCap;
    }
}
