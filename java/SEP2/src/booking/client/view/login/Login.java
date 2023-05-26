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
    @FXML private TextField txtVIAID;
    @FXML private PasswordField txtPassword;

    private LoginViewModel viewModel;

    public void init(LoginViewModel viewModel)
    {
        this.viewModel = viewModel;

        txtVIAID.textProperty().bindBidirectional(viewModel.VIAIDProperty());
        txtPassword.textProperty().bindBidirectional(viewModel.passwordProperty());
        btnLogIn.disableProperty().bind(viewModel.loginDisabledProperty());
        refresh();
    }

    private void refresh(){
        txtPassword.clear();
        txtVIAID.clear();
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
