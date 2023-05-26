package booking.client.viewModel.coordinatorGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelActiveUser;
import booking.client.model.ClientModelCoordinatorBooking;
import booking.client.model.ClientModelException;
import booking.client.model.ClientModelOverlapException;
import booking.client.viewModel.sharedVM.PredefinedColor;
import booking.client.viewModel.sharedVM.ViewModelUtil;
import booking.shared.CreateBookingParameters;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;

public class CoordinatorBookRoomViewModel
{
    private final ObservableList<String> timeIntervals;
    private final ObservableList<Character> buildings;
    private final ObservableList<Integer> floors;
    private final ObservableList<TimeSlot> preFixTimes;
    private final ObservableList<UserGroup> courses;
    private final ObservableList<Room> roomList;

    private final ObjectProperty<LocalDate> selectedStartDate;
    private final ObjectProperty<String> selectedFromTime;
    private final ObjectProperty<String> selectedToTime;
    private final ObjectProperty<TimeSlot> selectedPreFixTime;

    private final BooleanProperty prefixCheckBox;
    private final SimpleStringProperty selectedMinCap;
    private final SimpleStringProperty selectedMaxCap;
    private final ObjectProperty<Character> selectedBuilding;
    private final ObjectProperty<Integer> selectedFloor;
    private final ObjectProperty<UserGroup> selectedCourse;
    private final ObjectProperty<PredefinedColor> selectedCategory;

    private final ViewHandler viewHandler;
    private final ClientModelCoordinatorBooking bookingModel;
    private final CoordinatorViewModelState sharedState;

    public CoordinatorBookRoomViewModel(ViewHandler viewHandler, ClientModelCoordinatorBooking bookingModel, CoordinatorViewModelState sharedState)
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

        preFixTimes = FXCollections.observableArrayList();

        buildings = FXCollections.observableArrayList(
            null, 'A', 'B', 'C'
        );

        if (sharedState.getUser().getType().getId() == 1 || sharedState.getUser().getType().getId() == 4)
            floors = FXCollections.observableArrayList(null, 1, 2, 3, 4, 5, 6);
        else
            floors = FXCollections.observableArrayList(null, 1, 2, 3, 4, 5);

        courses = FXCollections.observableArrayList();

        selectedStartDate = new SimpleObjectProperty<>();
        selectedFromTime = new SimpleObjectProperty<>();
        selectedToTime = new SimpleObjectProperty<>();
        selectedPreFixTime = new SimpleObjectProperty<>();
        selectedMinCap = new SimpleStringProperty();
        selectedMaxCap = new SimpleStringProperty();
        selectedBuilding = new SimpleObjectProperty<>();
        selectedFloor = new SimpleObjectProperty<>();
        selectedCourse = new SimpleObjectProperty<>();
        selectedCategory = new SimpleObjectProperty<>();
        prefixCheckBox = new SimpleBooleanProperty();

        roomList = FXCollections.observableArrayList();

        try
        {
            preFixTimes.addAll(bookingModel.getTimeSlots());
            courses.addAll(bookingModel.getUserGroups());
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
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

    public ObjectProperty<PredefinedColor> selectedCategoryProperty()
    {
        return selectedCategory;
    }


    public SimpleStringProperty selectedMinCapProperty()
    {
        return selectedMinCap;
    }

    public SimpleStringProperty selectedMaxCapProperty()
    {
        return selectedMaxCap;
    }

    public ObjectProperty<Character> selectedBuildingProperty()
    {
        return selectedBuilding;
    }

    public Property<Boolean> prefixCheckBoxProperty()
    {
        return prefixCheckBox;
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
        //TODO Category
        try
        {
            Character building = selectedBuilding.get();
            Integer floor = selectedFloor.get();
            LocalDate date = selectedStartDate.get();

            // TODO(rune): timeIntervals list kunne evt. være med <LocalTime> i stedet,
            // så vi slipper for at parse her.

            LocalTime startTime = null;
            LocalTime endTime= null;

            if (prefixCheckBox.get()){
                startTime = selectedPreFixTime.get().getStart();
                endTime = selectedPreFixTime.get().getEnd();
            }

            else{
                String startTimeString = selectedFromTime.get();
                String endTimeString = selectedToTime.get();
                startTime = parseLocalDateTime(startTimeString);
                endTime = parseLocalDateTime(endTimeString);
            }
            if (startTime.isAfter(endTime)){
                viewHandler.showErrorDialog("Start time of booking must be before end time of booking");
            }
            else {
                // TODO(rune): Check om det virker rigtigt med, at building/floor er null, hvis ikke valgt.
                GetAvailableRoomsParameters parameters = new GetAvailableRoomsParameters(
                        date, startTime, endTime
                );

                parameters.setBuilding(building);
                parameters.setFloor(floor);
                if (!selectedMinCap.get().isEmpty())
                    parameters.setMinCapacity(Integer.parseInt(selectedMinCap.get()));
                if (!selectedMaxCap.get().isEmpty())
                    parameters.setMaxCapacity(Integer.parseInt(selectedMaxCap.get()) );

                roomList.clear();
                try
                {
                    roomList.addAll(bookingModel.getAvailableRooms(parameters));

                    if (selectedCategory.get() != null)
                    {
                        roomList.removeIf(room -> room.getUserColor() != selectedCategory.get().getArgb());
                    }
                }
                catch (ClientModelException e)
                {
                    viewHandler.showErrorDialog(e.getMessage());
                }
            }

        }
        catch (NumberFormatException e)
        {
            viewHandler.showErrorDialog("Max- and min capacity must be a number");
        }
        catch (NullPointerException e){
            viewHandler.showErrorDialog("Date and time interval must not be empty");
        }
        return roomList;


    }

    public void bookRoom(Room room)
    {
        // TODO(rune): timeIntervals liste kunne evt. være med <LocalTime> i stedet,
        // så vi slipper for at parse her.
        LocalTime startTime = parseLocalDateTime(selectedFromTime.get());
        LocalTime endTime = parseLocalDateTime(selectedToTime.get());
        UserGroup group = selectedCourse.get();
        BookingInterval requestedInterval = new BookingInterval(selectedStartDate.get(), startTime, endTime);

        CreateBookingParameters parameters = new CreateBookingParameters(
            room,
            requestedInterval,
            false,
            group
        );

        try
        {
            bookingModel.createBooking(parameters);
            viewHandler.showInfoDialog("Lokale " + room + " er booket til " + requestedInterval);
            sharedState.refreshActiveBookings();

            // NOTE(rune): Genindlæs så lokalet der lige er bliver booket forsvinder fra listen.
            showAvailableRooms();
        }
        catch (ClientModelOverlapException e)
        {
            viewHandler.showErrorDialog(
                ViewModelUtil.getOverlapsDisplayText(
                    e.getOverlaps()
                )
            );
        }
        catch (ClientModelException e)
        {
            throw new RuntimeException(e);
        }

    }

    private static LocalTime parseLocalDateTime(String s)
    {
        String[] parts = s.split(":");
        int minute = Integer.parseInt(parts[1]);
        int hour = Integer.parseInt(parts[0]);

        return LocalTime.of(hour, minute);
    }


    public void changeToSearch(String roomName)
    {
        viewHandler.showRoomInfo(roomName);
    }

    public ObservableList<PredefinedColor> getColors()
    {
        ObservableList<PredefinedColor> colors = FXCollections.observableArrayList();
        colors.add(null);
        colors.addAll(PredefinedColor.getPredefinedColors());

        return colors;
    }


}
