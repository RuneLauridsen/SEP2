package booking.client.view.login;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
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
    userTypes.addAll(model.getUserTypes());
    return userTypes;
  }

  public void showLogin()
  {
    viewHandler.showLogin();
  }

  public void register(int VIAID, String username, UserType userType, String password)
  {
    //TODO show some response if registered or failed
    model.register(username,password,null,VIAID,userType);
    viewHandler.showLogin();
  }

}