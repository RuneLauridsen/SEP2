package booking.client.view.login;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.HashingEncrypter;
import booking.shared.objects.User;
import booking.database.Persistence;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.security.NoSuchAlgorithmException;

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
        // TODO(rune): Bedre login. Server skal tjekke password.
        // Lige nu tjekker den kun brugernavnet.

        //TODO(julie) Jeg tror ikke at viewmodel skal være ansvarlig for at encrypt
        try {
            model.login(username.get(), HashingEncrypter.encrypt(password.get()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        // TODO(rune): Håndter login fejl

        //if (user != null)
        //{
            viewHandler.showUserBookRoom();
        //}
        //else
        //{
        //    viewHandler.showInfo("Forkert brugernavn eller adgangskode");
        //}
    }
}
