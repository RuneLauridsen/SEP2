package test;

import booking.client.view.CoordinatorGUI.CoordinatorBookingMenu;
import booking.client.view.CoordinatorGUI.CoordinatorHomeScreen;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorBookingMenuViewModel;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorHomeScreenViewModel;
import booking.client.viewModel.userGUIVM.UserHomeScreenViewModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static test.TestConstants.VIAID_GITTE;
import static test.TestConstants.VIAID_RUNE;

public class TestCase2 extends TestClientServerIntegrationBase
{
    @Test void scenario1()
    {
        // precondition
        loginAs(VIAID_RUNE);

        // step 1.1a
        assertEquals(viewHandler.getLatestView(), UserHomeScreenViewModel.class);
        UserHomeScreenViewModel homeScreen = viewModelFactory.getUserHomeScreenViewModel(viewHandler, client);
        assertEquals(homeScreen.getActiveBookings().size(), 2);
        assertEquals(homeScreen.getActiveBookings().get(0).toString(), "A02.02 2023-05-09 10:00-16:00 (Rune)");
        assertEquals(homeScreen.getActiveBookings().get(1).toString(), "A02.03 2023-05-10 10:00-16:00 (Rune)");

        // step 2 skip

        // step 3
        homeScreen.cancelBooking(homeScreen.getActiveBookings().get(0));

        // postcondition
        assertEquals(homeScreen.getActiveBookings().size(), 1);
        assertEquals(homeScreen.getActiveBookings().get(0).toString(), "A02.03 2023-05-10 10:00-16:00 (Rune)");
    }

    @Test void scenario2()
    {
        // precondition
        loginAs(VIAID_GITTE);

        // step 1.1b
        assertEquals(viewHandler.getLatestView(), CoordinatorHomeScreen.class);
        CoordinatorHomeScreenViewModel homeScreen = viewModelFactory.getCoordinatorHomeScreenViewModel(viewHandler, client);

        // step 2b
        homeScreen.changeToBooking();
        assertEquals(viewHandler.getLatestView(), CoordinatorBookingMenu.class);
        CoordinatorBookingMenuViewModel bookingMenu = viewModelFactory.getCoordinatorBookingMenuViewModel(viewHandler, client);
        assertEquals(bookingMenu.getBookings().size(), 4);
        assertEquals(bookingMenu.getBookings().get(0).toString(), "B02.04 2023-05-11 10:00-16:00 (Gitte)");
        assertEquals(bookingMenu.getBookings().get(1).toString(), "B02.05 2023-05-12 10:00-16:00 (Gitte)");
        assertEquals(bookingMenu.getBookings().get(2).toString(), "A03.02 2023-05-12 11:00-13:00 SDJ-2023 (by Gitte)");
        assertEquals(bookingMenu.getBookings().get(3).toString(), "A03.02 2023-05-12 16:00-16:45 SWE-2023 (by Gitte)");

        // step 3
        bookingMenu.cancelBooking(bookingMenu.getBookings().get(2));

        // postcondition
        assertEquals(bookingMenu.getBookings().size(), 3);
        assertEquals(bookingMenu.getBookings().get(0).toString(), "B02.04 2023-05-11 10:00-16:00 (Gitte)");
        assertEquals(bookingMenu.getBookings().get(1).toString(), "B02.05 2023-05-12 10:00-16:00 (Gitte)");
        assertEquals(bookingMenu.getBookings().get(2).toString(), "A03.02 2023-05-12 16:00-16:45 SWE-2023 (by Gitte)");

    }
}
