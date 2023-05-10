package booking;

import booking.database.Persistence;
import booking.view.userGUI.HomeScreenViewModel;
import booking.view.login.LoginViewModel;

import javax.swing.text.View;

public class ViewModelFactory
{
    public ViewModelFactory()
    {
    }

    public LoginViewModel getLoginViewModel(ViewHandler viewHandler, Persistence persistence)
    {
        return new LoginViewModel(viewHandler, persistence);
    }

    public HomeScreenViewModel getUserHomeScreenViewModel(ViewHandler viewHandler, Persistence persistence)
    {
        return new HomeScreenViewModel(viewHandler, persistence);
    }
}
