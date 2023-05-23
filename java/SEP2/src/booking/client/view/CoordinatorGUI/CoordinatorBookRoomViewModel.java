package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelOverlapException;
import booking.shared.CreateBookingParameters;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CoordinatorBookRoomViewModel
{
    private final ObservableList<String> timeIntervals;
    private final ObservableList<Character> buildings;
    private final ObservableList<Integer> floors;
    private final ObservableList<TimeSlot> preFixTimes;
    private final ObservableList<String> days;
    private final ObservableList<UserGroup> courses;
    private final ObservableList<Room> roomList;

    private final ObjectProperty<LocalDate> selectedStartDate;
    private final ObjectProperty<LocalDate> selectedEndDate;
    private final ObjectProperty<String> selectedFromTime;
    private final ObjectProperty<String> selectedToTime;
    private final ObjectProperty<TimeSlot> selectedPreFixTime;
    private final ObjectProperty<String> selectedDay;
    private final ObjectProperty<Integer> selectedMinCap;
    private final ObjectProperty<Integer> selectedMaxCap;
    private final ObjectProperty<Character> selectedBuilding;
    private final ObjectProperty<Integer> selectedFloor;
    private final ObjectProperty<UserGroup> selectedCourse;

    private final ViewHandler viewHandler;
    private final ClientModel model;

    public CoordinatorBookRoomViewModel(ViewHandler viewHandler, ClientModel model)
    {
        this.viewHandler = viewHandler;
        this.model = model;

        timeIntervals = FXCollections.observableArrayList(
            "7:00", "7:15", "7:30", "7:45", "8:00", "8:15", "8:30", "8:45",
            "9:00", "9:15", "9:30", "9:45", "10:00", "10:15", "10:30", "10:45",
            "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45",
            "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45",
            "15:00", "15:15", "15:30", "15:45", "16:00");

        preFixTimes = FXCollections.observableArrayList(
            model.getTimeSlots());

        days = FXCollections.observableArrayList(
            null, "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
        );

        buildings = FXCollections.observableArrayList(
            null, 'A', 'B', 'C'
        );

        floors = FXCollections.observableArrayList(
            null, 1, 2, 3, 4, 5, 6
        );

        courses = FXCollections.observableArrayList(
            model.getUserGroups()
        );

        selectedStartDate = new SimpleObjectProperty<>();
        selectedEndDate = new SimpleObjectProperty<>();
        selectedFromTime = new SimpleObjectProperty<>();
        selectedToTime = new SimpleObjectProperty<>();
        selectedPreFixTime = new SimpleObjectProperty<>();
        selectedDay = new SimpleObjectProperty<>();
        selectedMinCap = new SimpleObjectProperty<>();
        selectedMaxCap = new SimpleObjectProperty<>();
        selectedBuilding = new SimpleObjectProperty<>();
        selectedFloor = new SimpleObjectProperty<>();
        selectedCourse = new SimpleObjectProperty<>();

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

    public ObservableList<TimeSlot> getPreFixTimes()
    {
        return preFixTimes;
    }

    public ObservableList<String> getDays()
    {
        return days;
    }

    public ObservableList<UserGroup> getCourses()
    {
        return courses;
    }

    public ObservableList<Room> getRoomList()
    {
        return roomList;
    }

    public ObjectProperty<LocalDate> selectedStartDateProperty()
    {
        return selectedStartDate;
    }

    public ObjectProperty<LocalDate> selectedEndDateProperty()
    {
        return selectedEndDate;
    }

    public ObjectProperty<String> selectedFromTimeProperty()
    {
        return selectedFromTime;
    }

    public ObjectProperty<String> selectedToTimeProperty()
    {
        return selectedToTime;
    }

    public ObjectProperty<TimeSlot> selectedPreFixTimeProperty()
    {
        return selectedPreFixTime;
    }

    public ObjectProperty<String> selectedDayProperty()
    {
        return selectedDay;
    }

    public ObjectProperty<Integer> selectedMinCapProperty()
    {
        return selectedMinCap;
    }

    public ObjectProperty<Integer> selectedMaxCapProperty()
    {
        return selectedMaxCap;
    }

    public ObjectProperty<Character> selectedBuildingProperty()
    {
        return selectedBuilding;
    }

    public ObjectProperty<Integer> selectedFloorProperty()
    {
        return selectedFloor;
    }

    public ObjectProperty<UserGroup> selectedCourseProperty()
    {
        return selectedCourse;
    }

    public ObservableList<Room> showAvailableRooms()
    {
        // Comments er fra UserBookRoomViewModel
        // TODO: Min/max cap

        String startTimeString = selectedFromTime.get();
        String endTimeString = selectedToTime.get();
        Character building = selectedBuilding.get();
        Integer floor = selectedFloor.get();
        LocalDate date = selectedStartDate.get();

        // TODO(rune): timeIntervals list kunne evt. være med <LocalTime> i stedet,
        // så vi slipper for at parse her.
        LocalTime startTime = parseLocalDateTime(startTimeString);
        LocalTime endTime = parseLocalDateTime(endTimeString);
        BookingInterval requestedInterval = new BookingInterval(date, startTime, endTime);

        // TODO(rune): Check om det virker rigtigt med, at building/floor er null, hvis ikke valgt.
        GetAvailableRoomsParameters parameters = new GetAvailableRoomsParameters(
            date, startTime, endTime
        );

        parameters.setBuilding(building);
        parameters.setFloor(floor);

        List<Room> roomsFromDatabase = model.getAvailableRooms(parameters);
        roomList.removeAll();
        roomList.addAll(roomsFromDatabase);

        return roomList;

    }

    public void bookRoom(Room room)
    {
        // TODO(rune): timeIntervals liste kunne evt. være med <LocalTime> i stedet,
        // så vi slipper for at parse her.
        LocalTime startTime = parseLocalDateTime(selectedFromTime.get());
        LocalTime endTime = parseLocalDateTime(selectedToTime.get());
        BookingInterval requestedInterval = new BookingInterval(selectedStartDate.get(), startTime, endTime);

        CreateBookingParameters parameters = new CreateBookingParameters(
            room,
            requestedInterval,
            false,  // ingen overlap
            null    // ikke til nogne hold/klasse
        );

        try
        {
            model.createBooking(parameters);
        }
        catch (ClientModelOverlapException e)
        {
            // TODO(rune): Hvis fejlbesked
            throw new RuntimeException(e);
        }

        viewHandler.showInfoDialog("Lokale " + room + " er booking til " + requestedInterval);
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
        viewHandler.showRoomInfo(model.getRoom(roomName));
    }
}
