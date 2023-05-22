package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

public class CoordinatorBookingMenu
{
    @FXML
    private Button btnBookRoom;

    @FXML
    private Button btnInsertFile;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnConfirm;

    @FXML
    private Label lblFileName;

    @FXML
    private ListView<CoordinatorBookingMenu> lvBookings;

    @FXML
    private VBox VBoxFile;

    private CoordinatorBookingMenuViewModel viewModel;

    @FXML
    private void btnBookRoomClicked(ActionEvent event)
    {
        viewModel.bookRoomAction();
    }

    public void init(CoordinatorBookingMenuViewModel viewModel, ViewHandler viewHandler)
    {
        
    }

    public void btnInsertFileClicked(MouseEvent event)
    {

    }

    public void btnCancelClicked(MouseEvent event)
    {

    }

    public void btnConfirmClicked(MouseEvent event)
    {

    }

}
