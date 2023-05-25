package booking.client.view.login;

import booking.client.viewModel.loginVM.RegisterViewModel;
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


    txtVIAID.textProperty().bindBidirectional(viewModel.viaIDProperty());
    txtUsername.textProperty().bindBidirectional(viewModel.usernameProperty());
    txtPassword.textProperty().bindBidirectional(viewModel.passwordProperty());
    cbbUserType.valueProperty().bindBidirectional(viewModel.userTypeProperty());

  }

  public void onBackClicked(ActionEvent actionEvent)
  {
    viewModel.showLogin();
  }

  public void onRegisterClick(ActionEvent actionEvent)
  {
    viewModel.register();
  }
}
