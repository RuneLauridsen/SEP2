package test;

import booking.client.core.ViewHandler;
import booking.client.core.ViewModelFactory;
import booking.client.model.ClientModel;
import booking.client.view.CoordinatorGUI.CoordinatorBookRoom;
import booking.client.view.CoordinatorGUI.CoordinatorBookingMenu;
import booking.client.view.CoordinatorGUI.CoordinatorHomeScreen;
import booking.client.viewModel.coordinatorGUIVM.AddRoomViewModel;
import booking.client.viewModel.coordinatorGUIVM.EditRoomViewModel;
import booking.client.viewModel.loginVM.LoginViewModel;
import booking.client.viewModel.loginVM.RegisterViewModel;
import booking.client.viewModel.roomInfoVM.RoomInfoViewModel;
import booking.client.viewModel.userGUIVM.UserBookRoomViewModel;
import booking.client.viewModel.userGUIVM.UserHomeScreenViewModel;
import booking.shared.objects.Room;
import booking.shared.objects.User;

public class FakeViewHandler implements ViewHandler
{
    private String latestDialog;
    private Class latestView;
    private boolean okCancelChoice;
    private ViewModelFactory viewModelFactory;
    private ClientModel model;

    public FakeViewHandler(ViewModelFactory viewModelFactory, ClientModel model)
    {
        this.viewModelFactory = viewModelFactory;
        this.model = model;
    }

    public String getLatestDialog()
    {
        return latestDialog;
    }

    public Class getLatestView()
    {
        return latestView;
    }

    public void setOkCancelChoice(boolean okCancelChoice)
    {
        this.okCancelChoice = okCancelChoice;
    }

    public void showLogin()
    {
       latestView = LoginViewModel.class;
    }

    public void showUserBookRoom()
    {
        latestView = UserBookRoomViewModel.class;
    }

    public void showCoordinatorHomeScreen(User user)
    {
        latestView = CoordinatorHomeScreen.class;
    }

    public void showCoordinatorBookRoom()
    {
        latestView = CoordinatorBookRoom.class;
    }

    public void showCoordinatorBookingMenu()
    {
        latestView = CoordinatorBookingMenu.class;
    }

    public void showUserHomeScreen(User user)
    {
        latestView = UserHomeScreenViewModel.class;
    }

    public void showAddRoom()
    {
        latestView = AddRoomViewModel.class;
    }

    public void showRoomInfo(Room room)
    {
        latestView = RoomInfoViewModel.class;
    }

    public void showRegister()
    {
        latestView = RegisterViewModel.class;
    }

    public void showEditRoom(Room room)
    {
        latestView = EditRoomViewModel.class;
    }

    public void showInfoDialog(String text)
    {
        latestDialog = text;
    }

    public void showInfoDialog(String header, String content)
    {
        latestDialog = content;
    }

    public void showWarningDialog(String text)
    {
        latestDialog = text;
    }

    public void showWarningDialog(String header, String content)
    {
        latestDialog = content;
    }

    public void showErrorDialog(String text)
    {
        latestDialog = text;
    }

    public void showErrorDialog(String header, String content)
    {
        latestDialog = content;
    }

    @Override public boolean showOkCancelDialog(String header, String prompt)
    {
        latestDialog = prompt;
        return okCancelChoice;
    }
}

