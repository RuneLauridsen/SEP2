package booking.view.roomList;

import booking.core.Room;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class RoomListView
{
    @FXML private ListView<Room> listView;

    private RoomListViewModel viewModel;

    public void init(RoomListViewModel viewModel)
    {
        this.viewModel = viewModel;

        listView.setCellFactory(listView -> new RoomListCell());
        listView.setItems(viewModel.getRoomList());
    }
}
