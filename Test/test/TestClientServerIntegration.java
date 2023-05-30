package test;

import booking.client.core.ViewModelFactory;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelException;
import booking.client.networking.ClientNetworkException;
import booking.client.viewModel.loginVM.LoginViewModel;
import booking.client.viewModel.userGUIVM.UserBookRoomViewModel;
import booking.server.model.importFile.ImportFileResult;
import booking.shared.objects.Booking;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static test.TestConstants.VIAID_GITTE;
import static test.TestConstants.VIAID_RUNE;

// NOTE(rune): Tester hele vejen i gennem for både Client og server.
// TODO(rune): Test all use cases
// TODO(rune): Test zombies
public class TestClientServerIntegration
{
    private FakeViewHandler viewHandler;
    private ViewModelFactory viewModelFactory;
    private ClientModel model;
    private TestUtil.ServerInstance server;

    @BeforeEach void setup() throws ClientNetworkException
    {
        server = TestUtil.setupServer();
        model = TestUtil.setupClient();
        viewModelFactory = new ViewModelFactory();
        viewHandler = new FakeViewHandler(viewModelFactory, model);
    }

    @AfterEach void cleanup()
    {
        TestUtil.setdownServer(server);
    }

    @Test void testLogin()
    {
        LoginViewModel loginViewModel = viewModelFactory.getLoginViewModel(viewHandler, model);
        loginViewModel.VIAIDProperty().set(Integer.toString(VIAID_RUNE));
        loginViewModel.passwordProperty().set("abc");
        loginViewModel.loginAction();

        assertEquals(model.getUser().getName(), "Rune");
        assertEquals(model.getUser().getViaId(), 444444);
        assertEquals(model.getUser().getInitials(), null);
        assertEquals(model.getUser().getType().getName(), "Studerende");
    }

    @Test void testLogin_invalidCredentials()
    {
        LoginViewModel loginViewModel = viewModelFactory.getLoginViewModel(viewHandler, model);
        loginViewModel.VIAIDProperty().set(Integer.toString(VIAID_RUNE));
        loginViewModel.passwordProperty().set("forkert password");
        loginViewModel.loginAction();

        assertEquals(viewHandler.getLatestDialog(), "Invalid credentials");
    }

    @Test void testRegister()
    {
        // TODO(rune): Test register
    }

    /*
    @Test void testCreateBooking() throws ClientModelException
    {
        model.login(VIAID_GITTE, "1234");

        // NOTE(rune): Tjekker om vi finder de rigtige lokaler
        UserBookRoomViewModel viewModel = viewModelFactory.getUserBookRoomViewModel(viewHandler, model);
        viewModel.selectedDateProperty().set(LocalDate.of(2023, 5, 1));
        viewModel.selectedFromTimeProperty().set("11:00");
        viewModel.selectedToTimeProperty().set("13:00");
        viewModel.selectedBuildingProperty().set('A');
        viewModel.selectedFloorProperty().set(2);
        viewModel.showAvailableRooms();
        assertEquals(viewModel.getRoomList().size(), 3);
        assertEquals(viewModel.getRoomList().get(0).getName(), "A02.01");
        assertEquals(viewModel.getRoomList().get(1).getName(), "A02.02");
        assertEquals(viewModel.getRoomList().get(2).getName(), "A02.03");

        // NOTE(rune): Tjekker at booket lokale ikke længere bliver vist på listen
        viewModel.bookRoom(viewModel.getRoomList().get(0));
        assertEquals(viewModel.getRoomList().size(), 2);
        assertEquals(viewModel.getRoomList().get(0).getName(), "A02.02");
        assertEquals(viewModel.getRoomList().get(1).getName(), "A02.03");

        // NOTE(rune): Tjekker om booket lokale dukker på på listen igen.
        viewModel.selectedFromTimeProperty().set("13:00");
        viewModel.selectedToTimeProperty().set("14:00");
        viewModel.showAvailableRooms();
        assertEquals(viewModel.getRoomList().size(), 3);
        assertEquals(viewModel.getRoomList().get(0).getName(), "A02.01");
        assertEquals(viewModel.getRoomList().get(1).getName(), "A02.02");
        assertEquals(viewModel.getRoomList().get(2).getName(), "A02.03");
    }

     */

    @Test void testImportFile() throws ClientModelException
    {
        model.login(VIAID_GITTE, "1234");

        List<Booking> bookingsBefore = model.getBookingsForRoom("A02.01", LocalDate.MIN, LocalDate.MAX);
        ImportFileResult result = model.importFile("import.csv");
        List<Booking> bookingsAfter = model.getBookingsForRoom("A02.01", LocalDate.MIN, LocalDate.MAX);

        assertEquals(bookingsAfter.size(), bookingsBefore.size() + 2);
        assertEquals(result.getOverlaps().size(), 0);
    }

    @Test void testImportFile_withOverlap() throws ClientModelException
    {
        model.login(VIAID_GITTE, "1234");

        List<Booking> bookingsBefore = model.getBookingsForRoom("A02.01", LocalDate.MIN, LocalDate.MAX);
        ImportFileResult result = model.importFile("import_overlap.csv");
        List<Booking> bookingsAfter = model.getBookingsForRoom("A02.01", LocalDate.MIN, LocalDate.MAX);

        // Burde ikke lave nogle bookinger fordi der var overlap
        assertEquals(bookingsAfter.size(), bookingsBefore.size());

        assertEquals(result.getOverlaps().size(), 3);

        // Overlap med eksisterende bookinger
        assertEquals(result.getOverlaps().get(0).getRow(), 4);
        assertEquals(result.getOverlaps().get(0).getOverlap().getUsers().get(0).getName(), "Julie");
        assertEquals(result.getOverlaps().get(0).getOverlap().getUsers().get(1).getName(), "Simon");
        assertEquals(result.getOverlaps().get(0).getOverlap().getBooking().getUserGroup().getName(), "SDJ-2023");

        // Overlap med bookinger fra samme datafil
        assertEquals(result.getOverlaps().get(1).getRow(), 2);
        assertEquals(result.getOverlaps().get(2).getRow(), 5);
    }

    @Test void testImportFile_withOverlapAllowed() throws ClientModelException
    {
        model.login(VIAID_GITTE, "1234");

        List<Booking> bookingsBefore = model.getBookingsForRoom("A02.01", LocalDate.MIN, LocalDate.MAX);
        ImportFileResult result = model.importFile("import_overlap_allowed.csv");
        List<Booking> bookingsAfter = model.getBookingsForRoom("A02.01", LocalDate.MIN, LocalDate.MAX);

        assertEquals(bookingsAfter.size(), bookingsBefore.size() + 4);
        assertEquals(result.getOverlaps().size(), 0);
    }

    @Test void testImportFile_fileNotFound() throws ClientModelException
    {
        model.login(VIAID_GITTE, "1234");

        List<Booking> bookingsBefore = model.getBookingsForRoom("A02.01", LocalDate.MIN, LocalDate.MAX);
        ImportFileResult result = model.importFile("nonexistent.csv");
        List<Booking> bookingsAfter = model.getBookingsForRoom("A02.01", LocalDate.MIN, LocalDate.MAX);

        assertEquals(bookingsAfter.size(), bookingsBefore.size());
        assertEquals(result.getErrors().size(), 1);
        assertEquals(result.getErrors().get(0).toString(), "nonexistent.csv not found");
    }

    @Test void testImportFile_parseError() throws ClientModelException
    {
        model.login(VIAID_GITTE, "1234");

        List<Booking> bookingsBefore = model.getBookingsForRoom("A02.01", LocalDate.MIN, LocalDate.MAX);
        ImportFileResult result = model.importFile("import_parse_error.csv");
        List<Booking> bookingsAfter = model.getBookingsForRoom("A02.01", LocalDate.MIN, LocalDate.MAX);

        assertEquals(bookingsAfter.size(), bookingsBefore.size());
        assertEquals(result.getErrors().size(), 4);
        assertEquals(result.getErrors().get(0).toString(), "Row 2: Must have exactly 6 columns, but found 5");
        assertEquals(result.getErrors().get(1).toString(), "Row 3: Room not found");
        assertEquals(result.getErrors().get(2).toString(), "Row 4: Invalid date or time.");
        assertEquals(result.getErrors().get(3).toString(), "Row 5: Group not found.");
    }
}
