package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.server.model.importFile.ImportFileError;
import booking.server.model.importFile.ImportFileOverlap;
import booking.server.model.importFile.ImportFileResult;
import booking.shared.objects.User;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;

public class CoordinatorBookingMenuViewModel
{
    private ViewHandler viewHandler;
    private ClientModel model;

    public CoordinatorBookingMenuViewModel(ViewHandler viewHandler, ClientModel model)
    {
        this.viewHandler = viewHandler;
        this.model = model;
    }

    public void bookRoomAction()
    {
        viewHandler.showCoordinatorBookRoom();
    }

    public void insertFileAction()
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
                StringBuilder message = new StringBuilder();
                message.append("Bookings not imported:");
                message.append("\n");

                for (ImportFileError error : result.getErrors())
                {
                    message.append(error);
                    message.append("\n");
                }

                for (ImportFileOverlap overlap : result.getOverlaps())
                {
                    message.append(overlap);
                    message.append("\n");

                    for (User user : overlap.getOverlap().getUsers())
                    {
                        message.append("\t");
                        message.append(user);
                        message.append("\n");
                    }
                }

                viewHandler.showWarningDialog(message.toString());
            }
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
}
