package booking.client.view.login;

import booking.client.viewModel.loginVM.LoginViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Login
{
    @FXML private Button btnLogIn;
    @FXML private Button btnRegister;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    private LoginViewModel viewModel;

    public void init(LoginViewModel viewModel)
    {
        this.viewModel = viewModel;

        txtUsername.textProperty().bindBidirectional(viewModel.usernameProperty());
        txtPassword.textProperty().bindBidirectional(viewModel.passwordProperty());
        btnLogIn.disableProperty().bind(viewModel.loginDisabledProperty());
    }

    @FXML private void btnLoginClicked(ActionEvent actionEvent)
    {
        viewModel.loginAction();
    }

    public void btnRegisterClicked(ActionEvent actionEvent)
    {
        viewModel.showRegister();
    }
}
