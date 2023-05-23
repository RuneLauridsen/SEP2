package test;

import booking.client.core.ViewHandler;
import booking.shared.objects.Room;
import booking.shared.objects.User;

public class FakeViewHandler implements ViewHandler
{
    private String latestDialog;

    public String getLatestDialog()
    {
        return latestDialog;
    }

    public void showLogin()
    {

    }

    public void showUserBookRoom()
    {

    }

    public void showCoordinatorHomeScreen(User user)
    {

    }

    public void showCoordinatorBookRoom()
    {

    }

    public void showCoordinatorBookingMenu()
    {

    }

    public void showUserHomeScreen(User user)
    {

    }

    public void showAddRoom()
    {

    }

    public void showRoomInfo(Room room)
    {

    }

    public void showRegister()
    {

    }

    public void showEditRoom(Room room)
    {

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
}

