package booking.view.chooseDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.DateTimeStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.sql.DataTruncation;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

        datePicker
            .valueProperty()
            .bindBidirectional(viewModel.selectedDateProperty());

        timeTextFrom
            .textProperty()
            .bindBidirectional(viewModel.selectedFromHourProperty(), new NullableIntegerStringConverter());

        timeTextTo
            .textProperty()
            .bindBidirectional(viewModel.selectedToHourProperty(), new NullableIntegerStringConverter());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateStringConverter converter = new LocalDateStringConverter(formatter, null);
        datePicker.setConverter(converter);
    }

    public void showRoomsClicked(ActionEvent actionEvent)
    {
        viewModel.showRoomsCommand();
    }

    // NOTE(rune): IntegerStringConverter throws a NumberFormatException when
    // the input String cannot be parsed. Since we don't want exceptions every time
    // the user enters an invalid input String, NullableIntegerStringConverter just
    // returns null instead.
    private static class NullableIntegerStringConverter extends IntegerStringConverter
    {
        @Override public Integer fromString(String string)
        {
            try
            {
                return super.fromString(string);
            }
            catch (NumberFormatException e)
            {
                return null;
            }
        }
    }
}
