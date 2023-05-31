package booking.client.viewModel.coordinatorGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModelActiveBookings;
import booking.client.model.ClientModelException;
import booking.client.model.ClientModelImport;
import booking.client.viewModel.sharedVM.ViewModelUtil;
import booking.server.model.importFile.ImportFileResult;
import booking.shared.objects.Booking;
import booking.shared.objects.Room;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import java.io.File;

public class CoordinatorBookingMenuViewModel
{
    private final ViewHandler viewHandler;
    private final ClientModelImport importModel;
    private final ClientModelActiveBookings activeBookingsModel;
    private final CoordinatorViewModelState sharedState;

    public CoordinatorBookingMenuViewModel(ViewHandler viewHandler, ClientModelImport importModel, ClientModelActiveBookings activeBookingsModel, CoordinatorViewModelState sharedState)
    {
        this.viewHandler = viewHandler;
        this.importModel = importModel;
        this.activeBookingsModel = activeBookingsModel;
        this.sharedState = sharedState;
    }

    public void bookRoomAction()
    {
        viewHandler.showCoordinatorBookRoom();
    }

    public void insertFileAction(File file)
    {
        try
        {
            boolean b = file.canRead();

            if (file != null)
            {
                if (viewHandler.showOkCancelDialog("Import", "Vil du importere " + file.getName()))
                {
                    ImportFileResult result = importModel.importFile(file.getAbsolutePath());
                    if (result.isOk())
                    {
                        viewHandler.showInfoDialog("Bookings imported.");
                        sharedState.refreshActiveBookings();
                    }
                    else
                    {
                        // TODO: ImportFileResult burde have et view for sig, da det
                        // bliver uoverskueligt, hvis der er mange fejl og/eller overlap.
                        String message = ViewModelUtil.getImportFileResultDisplayText(result);
                        viewHandler.showWarningDialog(message);
                    }
                }
            }
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }

    public ObservableList<Booking> getBookings()
    {
        return sharedState.getActiveBookings();
    }

    public void cancelBooking(Booking booking)
    {
        try
        {
            activeBookingsModel.deleteBooking(booking);
            sharedState.refreshActiveBookings();
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }

    public void ChangeToSearch(Room room)
    {
        viewHandler.showRoomInfo(room);
    }
}
