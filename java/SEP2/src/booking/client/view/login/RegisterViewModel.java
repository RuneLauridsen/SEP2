package booking.client.view.login;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelException;
import booking.shared.objects.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RegisterViewModel
{
    ClientModel model;
    ViewHandler viewHandler;

    public RegisterViewModel(ViewHandler viewHandler, ClientModel model)
    {
        this.model = model;
        this.viewHandler = viewHandler;
    }

    public ObservableList<UserType> getUserTypes()
    {
        ObservableList<UserType> userTypes = FXCollections.observableArrayList();
        
        try
        {
            userTypes.addAll(model.getUserTypes());
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }

        return userTypes;
    }

    public void showLogin()
    {
        viewHandler.showLogin();
    }

    public void register(int VIAID, String username, UserType userType, String password)
    {
        //TODO show some response if registered or failed
        try
        {
            model.register(username, password, null, VIAID, userType);
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }

        viewHandler.showLogin();
    }

}
