package booking.client.view.CoordinatorGUI;

import booking.client.viewModel.coordinatorGUIVM.AddRoomViewModel;
import booking.shared.objects.RoomType;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AddRoom
{
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
    viewModel.createRoom(txtName.getText(), cbbRoomType.getSelectionModel().getSelectedItem(), Integer.parseInt(txtMaxComfortCap.getText()), Integer.parseInt(txtMaxSafetyCap.getText()), Integer.parseInt(txtSize.getText()), txtComment.getText(), cbIsDoubleRoom.isSelected(), txtDoubleRoomName.getText());
  }
}
