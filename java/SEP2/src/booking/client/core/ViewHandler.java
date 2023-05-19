package booking.client.core;

import booking.shared.objects.Room;
import booking.shared.objects.User;

// NOTE(rune): ViewHandler er nødt til at være et interface,
// fordi UnitTest'ene ikke skal begynde at loade fxml filer.
public interface ViewHandler
{
    public void showLogin();
    public void showUserBookRoom();
    public void showCoordinatorHomeScreen(User user);
    public void showCoordinatorBookRoom();
    public void showUserHomeScreen(User user);
    public void showAddRoom();
    public void showRoomInfo(Room room);
    public void showRegister();
    public void showEditRoom(Room room);

    public void showInfoDialog(String text);
    public void showInfoDialog(String header, String content);
    public void showWarningDialog(String text);
    public void showWarningDialog(String header, String content);
    public void showErrorDialog(String text);
    public void showErrorDialog(String header, String content);
}
