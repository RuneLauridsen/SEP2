package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;

public class CoordinatorHomeScreenViewModel
{
  ViewHandler viewHandler;
  public CoordinatorHomeScreenViewModel(ViewHandler viewHandler, ClientModel persistence)
  {
    this.viewHandler = viewHandler;
  }


  public void changeToAddRoom(){
    viewHandler.showAddRoom();
  }
}
