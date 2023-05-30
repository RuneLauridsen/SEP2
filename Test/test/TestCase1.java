package test;

import booking.client.model.ClientModelException;
import booking.client.viewModel.userGUIVM.UserBookRoomViewModel;
import booking.client.viewModel.userGUIVM.UserHomeScreenViewModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static test.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCase1 extends TestClientServerIntegrationBase
{
    public TestCase1()
    {
    }

    @Test void scenario1() throws ClientModelException
    {
        // precondition
        loginAs(VIAID_SIMON);
        UserHomeScreenViewModel homeScreen = viewModelFactory.getUserHomeScreenViewModel(viewHandler, client);
        UserBookRoomViewModel bookScreen = viewModelFactory.getUserBookRoomViewModel(viewHandler, client);

        // step 1
        homeScreen.changeToBooking();
        assertEquals(viewHandler.getLatestView(), UserBookRoomViewModel.class);

        // step 2.1
        bookScreen.selectedDateProperty().set(LocalDate.of(2023, 7, 1));

        // step 3.1
        bookScreen.selectedFromTimeProperty().set("09:00");
        bookScreen.selectedToTimeProperty().set("15:00");

        // step 4.1
        bookScreen.selectedMinCap().setValue("4");
        bookScreen.selectedMaxCap().setValue("4");

        // step 5.1
        bookScreen.selectedBuildingProperty().set('B');

        // step 6.1
        bookScreen.selectedFloorProperty().set(2);

        // step 7
        bookScreen.showAvailableRooms();
        assertEquals(bookScreen.getRoomList().get(0).getName(), "B02.04");

        // step 8 + 9
        viewHandler.setOkCancelChoice(true);
        bookScreen.bookRoom(bookScreen.getRoomList().get(0));

        // Post condition
        assertEquals(homeScreen.getActiveBookings().get(0).toString(), "B02.04 2023-07-01 09:00-15:00 (Simon)");
    }
}
