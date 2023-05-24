package booking.client.view.CoordinatorGUI;

import booking.shared.objects.RoomType;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class EditRoom
{
    public TextField txtName;
    public TextField txtMaxComfortCap;
    public TextField txtMaxSafetyCap;
    public TextField txtSize;
    public TextArea txaComment;
    public CheckBox cbIsDoubleRoom;
    public TextField txtDoubleRoomName;
    public ComboBox<RoomType> cbbRoomType;
    public Button cancelButton;
    public Button saveButton;
    public ComboBox<String> cbbColor;
    public TextArea txaPersonalComment;
    public Button btnDelete;

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
        txaComment.setText(viewModel.getRoom().getComment());
        cbbRoomType.setValue(viewModel.getRoom().getType());
        cbbColor.setValue(viewModel.getRoomColor());
    }

    public void saveButtonClick(MouseEvent mouseEvent)
    {
        viewModel.updateRoom(txtName.getText(), cbbRoomType.getSelectionModel().getSelectedItem(), Integer.parseInt(txtMaxComfortCap.getText()), Integer.parseInt(txtMaxSafetyCap.getText()), Integer.parseInt(txtSize.getText()), txaComment.getText(), cbIsDoubleRoom.isSelected(), txtDoubleRoomName.getText(), txaPersonalComment.getText(), cbbColor.getSelectionModel().getSelectedItem());

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
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
