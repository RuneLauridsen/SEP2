package booking.view.login;

import booking.ViewHandler;
import booking.core.User;
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
}
