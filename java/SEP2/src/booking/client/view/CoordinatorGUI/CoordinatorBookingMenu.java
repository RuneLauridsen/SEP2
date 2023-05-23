package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class CoordinatorBookingMenu
{
    @FXML private Button btnBookRoom;

    @FXML private Button btnInsertFile;

    @FXML private Button btnCancel;

    @FXML private Button btnConfirm;

    @FXML private Label lblFileName;

    @FXML private ListView<CoordinatorBookingMenu> lvBookings;

    @FXML private VBox VBoxFile;

    private CoordinatorBookingMenuViewModel viewModel;

    public void init(CoordinatorBookingMenuViewModel viewModel)
    {
        this.viewModel = viewModel;
    }

    @FXML private void btnBookRoomClicked(ActionEvent event)
    {
        viewModel.bookRoomAction();
    }

    @FXML private void btnInsertFileClicked(MouseEvent event)
    {
        viewModel.insertFileAction();
    }

    @FXML private void btnCancelClicked(MouseEvent event)
    {
        viewModel.cancelAction();
    }

    @FXML private void btnConfirmClicked(MouseEvent event)
    {
        viewModel.confirmAction();
    }
}
