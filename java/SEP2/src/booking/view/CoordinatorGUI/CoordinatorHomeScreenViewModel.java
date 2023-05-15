package booking.view.CoordinatorGUI;

import booking.ViewHandler;
import booking.database.Persistence;

public class CoordinatorHomeScreenViewModel
{
  ViewHandler viewHandler;
  public CoordinatorHomeScreenViewModel(ViewHandler viewHandler, Persistence persistence)
  {
    this.viewHandler = viewHandler;
  }


  public void changeToAddRoom(){
    viewHandler.showAddRoom();
  }
}
