package booking.client.view.CoordinatorGUI;

import booking.shared.objects.RoomType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AddRoom
{
  public TextField txtName;
  public TextField txtMaxComfortCap;
  public TextField txtMaxSafetyCap;
  public TextField txtSize;
  public TextArea txaComment;
  public CheckBox cbIsDoubleRoom;
  public TextField txtDoubleRoomName;
  public ComboBox<RoomType> cbbRoomType;

  AddRoomViewModel viewModel;

  public void init(AddRoomViewModel viewModel){
    this.viewModel = viewModel;
  }
  public void saveButtonClick(MouseEvent mouseEvent)
  {
    viewModel.createRoom(txtName.getText(), cbbRoomType.getSelectionModel().getSelectedItem(), Integer.parseInt(txtMaxComfortCap.getText()), Integer.parseInt(txtMaxSafetyCap.getText()), Integer.parseInt(txtSize.getText()),txaComment.getText(), cbIsDoubleRoom.isSelected(), txtDoubleRoomName.getText());
  }
}
