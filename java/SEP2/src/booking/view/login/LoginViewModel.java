package booking.view.login;

import booking.ViewHandler;
import booking.core.User;
import booking.database.Persistence;
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
    private final Persistence persistence;

    public LoginViewModel(ViewHandler viewHandler, Persistence persistence)
    {
        this.viewHandler = viewHandler;
        this.persistence = persistence;

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
        // TODO(rune): Bedre login. Server skal tjekke password.
        // Lige nu tjekker den kun brugernavnet.

        User user = persistence.getUser(username.get());
        if (user != null)
        {
            viewHandler.showUserBookRoom(user);
        }
        else
        {
            viewHandler.showInfo("Forkert brugernavn eller adgangskode");
        }
    }
}
