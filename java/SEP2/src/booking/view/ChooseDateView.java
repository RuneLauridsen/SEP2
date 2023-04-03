package booking.view;

import booking.core.BookingInterval;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChooseDateView
{
    // NOTE(rune): DET ER KEDELIGT :((

    // Functionality:
    //  TODO(rune): Add minutes

    // Validation:
    //  TODO(rune): Hour validation, from < to
    //  TODO(rune): Handle parse exception and show message to user

    @FXML private TextField timeTextFrom;
    @FXML private TextField timeTextTo;
    @FXML private DatePicker datePicker;

    private ChooseDateViewModel viewModel;

    public ChooseDateView()
    {

    }

    public void init(ChooseDateViewModel viewModel)
    {
        this.viewModel = viewModel;

        datePicker.valueProperty().addListener(this::updateViewModelProperties);

        timeTextFrom.textProperty().addListener(this::updateViewModelProperties);
        timeTextTo.textProperty().addListener(this::updateViewModelProperties);
    }

    // TODO(rune): Less hacky binding
    private void updateViewModelProperties(Observable observable)
    {
        int fromHours = Integer.parseInt(timeTextFrom.getText());
        int toHours = Integer.parseInt(timeTextTo.getText());

        LocalTime fromTime = LocalTime.of(fromHours, 0, 0);
        LocalTime toTime = LocalTime.of(toHours, 0, 0);
        LocalDate date = datePicker.getValue();

        viewModel.selectedIntervalProperty().set(new BookingInterval(date, fromTime, toTime));
    }

    public void showRoomsClicked(ActionEvent actionEvent)
    {
        viewModel.showRooms();
    }
}
