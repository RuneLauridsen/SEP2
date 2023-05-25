package booking.client.core;

import booking.client.model.ClientModel;
import booking.client.viewModel.coordinatorGUIVM.AddRoomViewModel;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorBookRoomViewModel;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorBookingMenuViewModel;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorViewModelState;
import booking.client.viewModel.coordinatorGUIVM.EditRoomViewModel;
import booking.client.viewModel.loginVM.RegisterViewModel;
import booking.client.viewModel.userGUIVM.UserBookRoomViewModel;
import booking.client.viewModel.userGUIVM.UserHomeScreenViewModel;
import booking.client.viewModel.userGUIVM.UserViewModelState;
import booking.shared.objects.Room;
import booking.client.viewModel.roomInfoVM.RoomInfoViewModel;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorHomeScreenViewModel;
import booking.client.viewModel.loginVM.LoginViewModel;

import javax.swing.text.View;

public class ViewModelFactory
{
    private LoginViewModel loginViewModel;
    private RegisterViewModel registerViewModel;

    private UserBookRoomViewModel userBookRoomViewModel;
    private UserHomeScreenViewModel userHomeScreenViewModel;
    private UserViewModelState userViewModelState;

    private CoordinatorHomeScreenViewModel coordinatorHomeScreenViewModel;
    private CoordinatorBookRoomViewModel coordinatorBookRoomViewModel;
    private CoordinatorBookingMenuViewModel coordinatorBookingMenuViewModel;
    private CoordinatorViewModelState coordinatorViewModelState;

    private RoomInfoViewModel roomInfoViewModel;

    public ViewModelFactory()
    {
    }

    public LoginViewModel getLoginViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (loginViewModel == null)
        {
            loginViewModel = new LoginViewModel(viewHandler, model);
        }
        return loginViewModel;
    }

    public UserViewModelState getUserViewModelState(ViewHandler viewHandler, ClientModel model)
    {
        if (userViewModelState == null)
            userViewModelState = new UserViewModelState(viewHandler, model);
        return userViewModelState;
    }

    public UserHomeScreenViewModel getUserHomeScreenViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (userHomeScreenViewModel == null)
            userHomeScreenViewModel = new UserHomeScreenViewModel(viewHandler, model, getUserViewModelState(viewHandler, model));
        return userHomeScreenViewModel;
    }

    public UserBookRoomViewModel getUserBookRoomViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (userBookRoomViewModel == null)
            userBookRoomViewModel = new UserBookRoomViewModel(viewHandler, model, getUserViewModelState(viewHandler, model));
        return userBookRoomViewModel;
    }

    public RoomInfoViewModel getRoomInfoViewModel(ViewHandler viewHandler, ClientModel model, Room room)
    {
        if (roomInfoViewModel == null)
            roomInfoViewModel = new RoomInfoViewModel(viewHandler, model, room);
        return roomInfoViewModel;
    }

    public CoordinatorViewModelState getCoordinatorViewModelState(ViewHandler viewHandler, ClientModel model)
    {
        if (coordinatorViewModelState == null)
            coordinatorViewModelState = new CoordinatorViewModelState(viewHandler, model);
        return coordinatorViewModelState;
    }

    public CoordinatorHomeScreenViewModel getCoordinatorHomeScreenViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (coordinatorHomeScreenViewModel == null)
            coordinatorHomeScreenViewModel = new CoordinatorHomeScreenViewModel(viewHandler, model, getCoordinatorViewModelState(viewHandler, model));
        return coordinatorHomeScreenViewModel;
    }

    public CoordinatorBookRoomViewModel getCoordinatorBookRoomViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (coordinatorBookRoomViewModel == null)
            coordinatorBookRoomViewModel = new CoordinatorBookRoomViewModel(viewHandler, model, getCoordinatorViewModelState(viewHandler, model));
        return coordinatorBookRoomViewModel;
    }

    public CoordinatorBookingMenuViewModel getCoordinatorBookingMenuViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (coordinatorBookingMenuViewModel == null)
            coordinatorBookingMenuViewModel = new CoordinatorBookingMenuViewModel(viewHandler, model, getCoordinatorViewModelState(viewHandler, model));

        return coordinatorBookingMenuViewModel;
    }

    public AddRoomViewModel getAddRoomViewModel(ViewHandler viewHandler, ClientModel model)
    {
        // NOTE(rune): Skal altid åbnes i nyt vindue
        return new AddRoomViewModel(viewHandler, model, getCoordinatorViewModelState(viewHandler, model));
    }

    public EditRoomViewModel getEditRoomViewModel(ViewHandler viewHandler, ClientModel model, Room room)
    {
        // NOTE(rune): Skal altid åbnes i nyt vindue
        return new EditRoomViewModel(viewHandler, model, getCoordinatorViewModelState(viewHandler, model), room);
    }

    public RegisterViewModel getRegisterViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (registerViewModel == null)
            registerViewModel = new RegisterViewModel(viewHandler, model);
        return registerViewModel;
    }
}
