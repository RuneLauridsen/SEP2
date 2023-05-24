package booking.client.view.login;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelException;
import booking.shared.objects.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginViewModel
{

    private final StringProperty username;
    private final StringProperty password;
    private final BooleanProperty loginDisabled;
    private final ViewHandler viewHandler;
    private final ClientModel model;

    public LoginViewModel(ViewHandler viewHandler, ClientModel model)
    {
        this.viewHandler = viewHandler;
        this.model = model;

        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        loginDisabled = new SimpleBooleanProperty();
        loginDisabled.bind(username.isEmpty().or(password.isEmpty()));
    }

    public StringProperty usernameProperty()
    {
        return username;
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
            model.login(Integer.parseInt(username.get()), password.get());

            User user = model.getUser();
            
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
    }

    public void showRegister()
    {
        viewHandler.showRegister();
    }
}
