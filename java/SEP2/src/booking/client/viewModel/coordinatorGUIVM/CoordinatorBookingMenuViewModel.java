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
    private final ViewHandler viewHandler;
    private final ClientModel model;
    private final CoordinatorViewModelState sharedState;

    public CoordinatorBookingMenuViewModel(ViewHandler viewHandler, ClientModel model, CoordinatorViewModelState sharedState)
    {
        this.viewHandler = viewHandler;
        this.model = model;
        this.sharedState = sharedState;
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
                    sharedState.refreshActiveBookings();
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
        return sharedState.getActiveBookings();
    }

    public void cancelBooking(Booking booking)
    {
        try
        {
            model.deleteBooking(booking);
            sharedState.refreshActiveBookings();
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
