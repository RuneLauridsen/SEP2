package booking.view.userGUI;

import booking.ViewHandler;
import booking.core.BookingInterval;
import booking.core.Room;
import booking.core.User;
import booking.database.Persistence;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.converter.LocalTimeStringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class UserBookRoomViewModel
{
    private ObservableList<String> timeIntervals;
    private ObservableList<Character> buildings;
    private ObservableList<Integer> floors;

    private final ObjectProperty<LocalDate> selectedDate;
    private final ObjectProperty<String> selectedFromTime;
    private final ObjectProperty<String> selectedToTime;
    private final ObjectProperty<Character> selectedBuilding;
    private final ObjectProperty<Integer> selectedFloor;

    private ViewHandler viewHandler;
    private Persistence persistence;

    private User user;

    private ObservableList<Room> roomList;

    public UserBookRoomViewModel(ViewHandler viewHandler, Persistence persistence)
    {
        this.viewHandler = viewHandler;
        this.persistence = persistence;

        timeIntervals = FXCollections.observableArrayList(
            "7:00", "7:15", "7:30", "7:45", "8:00", "8:15", "8:30", "8:45",
            "9:00", "9:15", "9:30", "9:45", "10:00", "10:15", "10:30", "10:45",
            "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45",
            "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45",
            "15:00", "15:15", "15:30", "15:45", "16:00");

        buildings = FXCollections.observableArrayList('A', 'B', 'C');

        //TODO only 5 floors for normal
        floors = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6);

        selectedDate = new SimpleObjectProperty<>();
        selectedFromTime = new SimpleObjectProperty<>();
        selectedToTime = new SimpleObjectProperty<>();
        selectedBuilding = new SimpleObjectProperty<>();
        selectedFloor = new SimpleObjectProperty<>();

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

    public void setUser(User user)
    {
        this.user = user;
    }

    public ObservableList<Room> getRoomList()
    {
        return roomList;
    }

    public ObservableList<Room> showAvailablerooms()
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
        BookingInterval requestedInterval = new BookingInterval(date, startTime, endTime);

        // TODO(rune): Check om det virker rigtigt med, at building/floor er null, hvis ikke valgt.
        List<Room> roomsFromDatabase = persistence.getAvailableRooms(user, requestedInterval, null, null, building, floor);
        roomList.removeAll();
        roomList.addAll(roomsFromDatabase);

        return roomList;
    }

    private static LocalTime parseLocalDateTime(String s)
    {
        // TODO(rune): Argument validation

        String[] parts = s.split(":");
        int minute = Integer.parseInt(parts[1]);
        int hour = Integer.parseInt(parts[0]);

        return LocalTime.of(hour, minute);
    }

}
