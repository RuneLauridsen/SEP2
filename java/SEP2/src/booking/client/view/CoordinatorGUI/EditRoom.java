package booking.client.view.CoordinatorGUI;

import booking.shared.objects.RoomType;
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

  EditRoomViewModel viewModel;

  public void init(EditRoomViewModel viewModel){
    this.viewModel = viewModel;
    cbbRoomType.setItems(viewModel.getRoomTypes());

    txtName.setText(viewModel.getRoom().getName());
    txtMaxComfortCap.setText(String.valueOf(viewModel.getRoom().getComfortCapacity()));
    txtMaxSafetyCap.setText(String.valueOf(viewModel.getRoom().getFireCapacity()));
    txtSize.setText(String.valueOf(viewModel.getRoom().getSize()));
    txaComment.setText(viewModel.getRoom().getComment());
    cbbRoomType.setValue(viewModel.getRoom().getType());
  }

  public void saveButtonClick(MouseEvent mouseEvent)
  {
    viewModel.UpdateRoom(txtName.getText(), cbbRoomType.getSelectionModel().getSelectedItem(), Integer.parseInt(txtMaxComfortCap.getText()), Integer.parseInt(txtMaxSafetyCap.getText()), Integer.parseInt(txtSize.getText()),txaComment.getText(), cbIsDoubleRoom.isSelected(), txtDoubleRoomName.getText(), txaPersonalComment.getText(), cbbColor.getSelectionModel().getSelectedItem());


    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();
  }

  public void cancelButtonClick(MouseEvent mouseEvent)
  {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }
}
