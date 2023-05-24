package booking.client.viewModel.coordinatorGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelException;
import booking.client.viewModel.sharedVM.ViewModelUtil;
import booking.server.model.importFile.ImportFileResult;
import javafx.stage.FileChooser;

import java.io.File;

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

    public void insertFileAction()
    {
        try
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("csv", "*.csv")
            );
            File file = fileChooser.showOpenDialog(null);

            if (file != null)
            {
                ImportFileResult result = model.importFile(file.getAbsolutePath());
                if (result.isOk())
                {
                    viewHandler.showInfoDialog("Bookings imported.");
                }
                else
                {
                    // TODO(rune): ImportFileResult burde have et view for sig, da det
                    // bliver uoverskueligt, hvis der er mange fejl og/eller overlap.
                    String message = ViewModelUtil.getImportFileResultDisplayText(result);
                    viewHandler.showWarningDialog(message);
                }
            }
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }

    public void cancelAction()
    {
        // TODO(rune): Hvad skal cancel gøre?
    }

    public void confirmAction()
    {
        // TODO(rune): Hvad skal confirm gøre?
    }

    public ObservableList<Booking> getBookings()
    {
        bookings = FXCollections.observableArrayList();

        try
        {
            bookings.addAll(model.getActiveBookings());
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }

        return bookings;
    }

    public void refreshBookings()
    {
        // TODO(rune): Fælles ViewModelState

        bookings.clear();

        try
        {
            bookings.addAll(model.getActiveBookings());
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }

    public void cancelBooking(Booking booking)
    {
        try
        {
            model.deleteBooking(booking);
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }

    public void ChangeToSearch(String name)
    {
        try
        {
            viewHandler.showRoomInfo(model.getRoom(name));
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }
}
