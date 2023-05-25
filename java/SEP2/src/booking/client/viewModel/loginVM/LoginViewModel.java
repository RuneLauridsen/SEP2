package booking.client.viewModel.loginVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelActiveUser;
import booking.client.model.ClientModelException;
import booking.client.model.ClientModelLogin;
import booking.shared.objects.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginViewModel
{

    private final StringProperty VIAID;
    private final StringProperty password;
    private final BooleanProperty loginDisabled;
    private final ViewHandler viewHandler;
    private final ClientModelLogin loginModel;

    public LoginViewModel(ViewHandler viewHandler, ClientModelLogin loginModel)
    {
        this.viewHandler = viewHandler;
        this.loginModel = loginModel;

        VIAID = new SimpleStringProperty();
        password = new SimpleStringProperty();
        loginDisabled = new SimpleBooleanProperty();
        loginDisabled.bind(VIAID.isEmpty().or(password.isEmpty()));
    }

    public StringProperty VIAIDProperty()
    {
        return VIAID;
    }

    public StringProperty passwordProperty()
    {
        return password;
    }

    public BooleanProperty loginDisabledProperty()
    {
        return loginDisabled;
    }

    public void loginAction()
    {
        // TODO(rune): Input validering, eks. "abc" kan ikke parses som int

        try
        {
            model.login(Integer.parseInt(VIAID.get()), password.get());

            if (user.getType().getId() == 1)
            {
                viewHandler.showCoordinatorHomeScreen(user);
            }
            else
            {
                viewHandler.showUserHomeScreen(user);
            }
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
        catch (NumberFormatException e){
            viewHandler.showErrorDialog("VIA ID must be a number");
        }
    }

    public void showRegister()
    {
        viewHandler.showRegister();
    }
}
