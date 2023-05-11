package booking;

import booking.core.Room;
import booking.database.Persistence;
import booking.view.roomInfo.RoomInfoViewModel;
import booking.view.userGUI.*;
import booking.view.login.LoginViewModel;

public class ViewModelFactory
{
    private LoginViewModel loginViewModel;
    private UserBookRoomViewModel userBookRoomViewModel;
    private UserHomeScreenViewModel userHomeScreenViewModel;

    private RoomInfoViewModel roomInfoViewModel;
    public ViewModelFactory()
    {
    }

    public LoginViewModel getLoginViewModel(ViewHandler viewHandler, Persistence persistence)
    {
        if (loginViewModel == null){
            loginViewModel = new LoginViewModel(viewHandler, persistence);
        }
        return loginViewModel;
    }

    public UserHomeScreenViewModel getUserHomeScreenViewModel(ViewHandler viewHandler, Persistence persistence)
    {
        if (userHomeScreenViewModel == null)
            userHomeScreenViewModel = new UserHomeScreenViewModel(viewHandler, persistence);
        return userHomeScreenViewModel;
    }
    public UserBookRoomViewModel getUserBookRoomViewModel(ViewHandler viewHandler, Persistence persistence)
    {
        if (userBookRoomViewModel == null)
            userBookRoomViewModel = new UserBookRoomViewModel(viewHandler, persistence);
        return userBookRoomViewModel;
    }

    public RoomInfoViewModel getRoomInfoViewModel (ViewHandler viewHandler, Persistence persistence,
        Room room)
    {
        if (roomInfoViewModel == null)
            roomInfoViewModel = new RoomInfoViewModel(viewHandler, persistence, room);
        return roomInfoViewModel;
    }
}
