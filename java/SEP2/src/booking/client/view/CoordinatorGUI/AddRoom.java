package booking.client.view.CoordinatorGUI;

import booking.client.viewModel.coordinatorGUIVM.AddRoomViewModel;
import booking.shared.objects.RoomType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AddRoom
{
  public Button saveButton;
  @FXML private TextField txtName;
  @FXML private TextField txtMaxComfortCap;
  @FXML private TextField txtMaxSafetyCap;
  @FXML private TextField txtSize;
  @FXML private TextArea txtComment;
  @FXML private CheckBox cbIsDoubleRoom;
  @FXML private TextField txtDoubleRoomName;
  @FXML private ComboBox<RoomType> cbbRoomType;

  AddRoomViewModel viewModel;

  public void init(AddRoomViewModel viewModel){

    this.viewModel = viewModel;
    cbbRoomType.setItems(viewModel.getRoomTypes());
  }
  public void saveButtonClick(MouseEvent mouseEvent)
  {
    if(viewModel.createRoom(txtName.getText(), cbbRoomType.getSelectionModel().getSelectedItem(), txtMaxComfortCap.getText(), txtMaxSafetyCap.getText(), txtSize.getText(), txtComment.getText(), cbIsDoubleRoom.isSelected(), txtDoubleRoomName.getText())){
      Stage stage = (Stage) saveButton.getScene().getWindow();
      stage.close();
    }
  }
}
