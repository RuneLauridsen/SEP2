package booking.client.viewModel.loginVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModelException;
import booking.client.model.ClientModelRegister;
import booking.shared.objects.UserType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RegisterViewModel
{
    private final ClientModelRegister registerModel;
    private final ViewHandler viewHandler;


    private final ObjectProperty<String> usernameProperty;
    private final ObjectProperty<UserType> userTypeProperty;
    private final ObjectProperty<String> passwordProperty;
    private final ObjectProperty<String> viaIDProperty;

    public RegisterViewModel(ViewHandler viewHandler, ClientModelRegister model)

    {
        this.registerModel = model;
        this.viewHandler = viewHandler;

        usernameProperty = new SimpleObjectProperty<>();
        userTypeProperty = new SimpleObjectProperty<>();
        passwordProperty = new SimpleObjectProperty<>();
        viaIDProperty = new SimpleObjectProperty<>();
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

    public void register()
    {
        try
        {
            if(!viaIDProperty.get().matches("\\d{6}"))
            {
                viewHandler.showErrorDialog("VIA ID must be 6 digits");
                return;
            }

            if(!usernameProperty.get().trim().matches("[A-Za-z'\\- ]+"))
            {
                viewHandler.showErrorDialog("Invalid username");
                return;
            }

            if(userTypeProperty.get() == null)
            {
                viewHandler.showErrorDialog("No occupation selected");
                return;
            }

            registerModel.register(usernameProperty.get(), passwordProperty.get(), null, Integer.parseInt(viaIDProperty.get()) , userTypeProperty.get());
            viewHandler.showLogin();
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
        catch (NumberFormatException e)
        {
            viewHandler.showErrorDialog("VIA ID must be a number");
        }

    }

    public Property<String> viaIDProperty()
    {
        return viaIDProperty;
    }

    public Property<String> usernameProperty()
    {
        return  usernameProperty;
    }

    public Property<String> passwordProperty()
    {
        return passwordProperty;
    }

    public Property<UserType> userTypeProperty()
    {
        return userTypeProperty;
    }
}
