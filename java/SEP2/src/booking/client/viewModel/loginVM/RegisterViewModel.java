package booking.client.viewModel.loginVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModelException;
import booking.client.model.ClientModelRegister;
import booking.shared.objects.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RegisterViewModel
{
    private final ClientModelRegister registerModel;
    private final ViewHandler viewHandler;

    public RegisterViewModel(ViewHandler viewHandler, ClientModelRegister model)
    {
        this.registerModel = model;
        this.viewHandler = viewHandler;
    }

    public ObservableList<UserType> getUserTypes()
    {
        ObservableList<UserType> userTypes = FXCollections.observableArrayList();

        try
        {
            userTypes.addAll(registerModel.getUserTypes());
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
            registerModel.register(username, password, null, VIAID, userType);
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }

        viewHandler.showLogin();
    }

}
