package booking.client.view.login;

import booking.shared.objects.UserType;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Register
{

  public TextField txtVIAID;
  public TextField txtUsername;
  public ComboBox<UserType> cbbUserType;
  public PasswordField txtPassword;

  RegisterViewModel viewModel;
  public void init(RegisterViewModel viewModel)
  {
    this.viewModel = viewModel;
    cbbUserType.setItems(viewModel.getUserTypes());
  }

  public void onBackClicked(ActionEvent actionEvent)
  {
    viewModel.showLogin();
  }

  public void onRegisterClick(ActionEvent actionEvent)
  {
    viewModel.register(Integer.parseInt(txtVIAID.getText()), txtUsername.getText(), cbbUserType.getSelectionModel().getSelectedItem(), txtPassword.getText());
  }
}
