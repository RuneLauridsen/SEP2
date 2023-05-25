package booking.client.view.CoordinatorGUI;

import booking.client.viewModel.coordinatorGUIVM.EditRoomViewModel;
import booking.client.viewModel.sharedVM.PredefinedColor;
import booking.shared.objects.RoomType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class EditRoom
{
    @FXML private TextField txtName;
    @FXML private TextField txtMaxComfortCap;
    @FXML private TextField txtMaxSafetyCap;
    @FXML private TextField txtSize;
    @FXML private TextArea txtComment;
    @FXML private CheckBox cbIsDoubleRoom;
    @FXML private TextField txtDoubleRoomName;
    @FXML private ComboBox<RoomType> cbbRoomType;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private ComboBox<PredefinedColor> cbbColor;
    @FXML private TextArea txtPersonalComment;
    @FXML private Button btnDelete;

    EditRoomViewModel viewModel;

    public void init(EditRoomViewModel viewModel)
    {
        this.viewModel = viewModel;
        cbbRoomType.setItems(viewModel.getRoomTypes());
        cbbColor.setItems(viewModel.getColors());

        txtName.setText(viewModel.getRoom().getName());
        txtMaxComfortCap.setText(String.valueOf(viewModel.getRoom().getComfortCapacity()));
        txtMaxSafetyCap.setText(String.valueOf(viewModel.getRoom().getFireCapacity()));
        txtSize.setText(String.valueOf(viewModel.getRoom().getSize()));
        txtComment.setText(viewModel.getRoom().getComment());
        cbbRoomType.setValue(viewModel.getRoom().getType());
        cbbColor.setValue(viewModel.getRoomColor());
    }

    public void saveButtonClick(MouseEvent mouseEvent)
    {
        if(viewModel.updateRoom(txtName.getText(), cbbRoomType.getSelectionModel().getSelectedItem(), txtMaxComfortCap.getText(), txtMaxSafetyCap.getText(), txtSize.getText(), txtComment.getText(), cbIsDoubleRoom.isSelected(), txtDoubleRoomName.getText(), txtPersonalComment.getText(), cbbColor.getSelectionModel().getSelectedItem())){
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }

    }

    public void cancelButtonClick(MouseEvent mouseEvent)
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void deleteButtonClicked(ActionEvent actionEvent)
    {
        if (viewModel.deleteRoom() == true)
        {
            Stage stage = (Stage) btnDelete.getScene().getWindow();
            stage.close();
        }

    }
}
