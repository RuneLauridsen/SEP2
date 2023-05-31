package test;

import booking.client.view.CoordinatorGUI.CoordinatorBookingMenu;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorBookingMenuViewModel;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorHomeScreenViewModel;
import booking.server.persistene.PersistenceException;
import booking.shared.objects.Booking;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static test.TestConstants.VIAID_GITTE;

public class TestCase9 extends TestClientServerIntegrationBase
{
    private CoordinatorHomeScreenViewModel homeScreen;
    private CoordinatorBookingMenuViewModel bookingMenu;

    private String fileName;

    @Test void scenario1()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        postcondition(6);
    }

    @Test void scenario2()
    {
        precondition();
        step1();
        step2();
        step3_2();
        step4_2("""
Bookings not imported:
Row 4: A03.02 2023-05-12 11:00-13:00 SDJ-2023 (by Gitte)
	Julie
	Simon
Row 2: A02.01 2023-05-22 10:00-12:00 (Gitte)
	Gitte
Row 5: A02.01 2023-05-22 08:00-15:00 (Gitte)
	Gitte
""");
        postcondition(4);
    }

    @Test void scenario3()
    {
        precondition();
        step1();
        step2();
        step3_3();
        step4_1();
        postcondition(8);
    }

    @Test void scenario4()
    {
        precondition();
        step1();
        step2();
        step3_4();
        step4_2("""
Bookings not imported:
Row 2: Must have exactly 6 columns, but found 5
Row 3: Room not found
Row 4: Invalid date or time.
Row 5: Group not found.
""");
        postcondition(4);
    }

    @Test void scenario5()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_3();
        postcondition(4);
    }

    private void precondition()
    {
        loginAs(VIAID_GITTE);
    }

    private void step1()
    {
        homeScreen = viewModelFactory.getCoordinatorHomeScreenViewModel(viewHandler, client);
    }

    private void step2()
    {
        homeScreen.changeToBooking();
        assertEquals(viewHandler.getLatestView(), CoordinatorBookingMenu.class);

        bookingMenu = viewModelFactory.getCoordinatorBookingMenuViewModel(viewHandler, client);
    }

    private void step3_1()
    {
        fileName = "import.csv";
    }

    private void step3_2()
    {
        fileName = "import_overlap.csv";
    }

    private void step3_3()
    {
        fileName = "import_overlap_allowed.csv";
    }

    private void step3_4()
    {
        fileName = "import_parse_error.csv";
    }

    private void step4_1()
    {
        viewHandler.setOkCancelChoice(true);

        bookingMenu.insertFileAction(new File(fileName));
    }

    private void step4_2(String message)
    {
        viewHandler.setOkCancelChoice(true);

        bookingMenu.insertFileAction(new File(fileName));

        assertEquals(viewHandler.getLatestDialog(), message);
    }

    private void step4_3()
    {
        viewHandler.setOkCancelChoice(false);

        bookingMenu.insertFileAction(new File(fileName));
    }

    private void postcondition(int count)
    {
        try
        {
            List<Booking> bookings = server.database().getBookingsForUser(client.getUser(), LocalDate.MIN, LocalDate.MAX, null);
            assertEquals(bookings.size(), count);
        }
        catch (PersistenceException e)
        {
            throw new RuntimeException(e);
        }
    }
}
