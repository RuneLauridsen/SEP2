package booking.client.core;

import booking.client.model.ClientModel;
import booking.client.viewModel.coordinatorGUIVM.*;
import booking.client.viewModel.loginVM.LoginViewModel;
import booking.client.viewModel.loginVM.RegisterViewModel;
import booking.client.viewModel.sharedVM.RoomInfoViewModel;
import booking.client.viewModel.userGUIVM.UserBookRoomViewModel;
import booking.client.viewModel.userGUIVM.UserHomeScreenViewModel;
import booking.client.viewModel.userGUIVM.UserViewModelState;
import booking.shared.objects.Room;

public class ViewModelFactory
{
    private LoginViewModel loginViewModel;
    private RegisterViewModel registerViewModel;
    private AddRoomViewModel addRoomViewModel;


    private UserBookRoomViewModel userBookRoomViewModel;
    private UserHomeScreenViewModel userHomeScreenViewModel;
    private UserViewModelState userViewModelState;

    private CoordinatorHomeScreenViewModel coordinatorHomeScreenViewModel;
    private CoordinatorBookRoomViewModel coordinatorBookRoomViewModel;
    private CoordinatorBookingMenuViewModel coordinatorBookingMenuViewModel;
    private CoordinatorViewModelState coordinatorViewModelState;


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
            userViewModelState = new UserViewModelState(viewHandler, model, model);
        return userViewModelState;
    }

    public UserHomeScreenViewModel getUserHomeScreenViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (userHomeScreenViewModel == null)
            userHomeScreenViewModel = new UserHomeScreenViewModel(viewHandler, model, model, getUserViewModelState(viewHandler, model));
        return userHomeScreenViewModel;
    }

    public UserBookRoomViewModel getUserBookRoomViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (userBookRoomViewModel == null)
            userBookRoomViewModel = new UserBookRoomViewModel(viewHandler, model, getUserViewModelState(viewHandler, model));
        return userBookRoomViewModel;
    }


    public CoordinatorViewModelState getCoordinatorViewModelState(ViewHandler viewHandler, ClientModel model)
    {
        if (coordinatorViewModelState == null)
            coordinatorViewModelState = new CoordinatorViewModelState(viewHandler, model, model, model);
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
            coordinatorBookingMenuViewModel = new CoordinatorBookingMenuViewModel(viewHandler, model, model, getCoordinatorViewModelState(viewHandler, model));

        return coordinatorBookingMenuViewModel;
    }

    public AddRoomViewModel getAddRoomViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (addRoomViewModel == null)
            addRoomViewModel = new AddRoomViewModel(viewHandler, model, getCoordinatorViewModelState(viewHandler, model));
        return addRoomViewModel;
    }

    public EditRoomViewModel getEditRoomViewModel(ViewHandler viewHandler, ClientModel model, Room room)
    {
        // NOTE(rune): Skal altid åbnes i nyt vindue
        return new EditRoomViewModel(viewHandler, model, getCoordinatorViewModelState(viewHandler, model), room);
    }
    public RoomInfoViewModel getRoomInfoViewModel(ViewHandler viewHandler, ClientModel model, Room room)
    {
        // NOTE: Skal altid åbnes i nyt vindue
        return new RoomInfoViewModel(viewHandler, model, room);
    }

    public RegisterViewModel getRegisterViewModel(ViewHandler viewHandler, ClientModel model)
    {
        if (registerViewModel == null)
            registerViewModel = new RegisterViewModel(viewHandler, model);
        return registerViewModel;
    }
}
