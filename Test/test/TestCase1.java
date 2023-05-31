package test;

import booking.client.model.ClientModelException;
import booking.client.view.CoordinatorGUI.CoordinatorBookRoom;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorBookRoomViewModel;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorHomeScreenViewModel;
import booking.client.viewModel.userGUIVM.UserBookRoomViewModel;
import booking.client.viewModel.userGUIVM.UserHomeScreenViewModel;
import booking.server.persistene.PersistenceException;
import booking.shared.objects.Booking;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static test.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCase1 extends TestClientServerIntegrationBase
{
    private CoordinatorHomeScreenViewModel coordinatorHomeScreen;
    private UserBookRoomViewModel userBookRoom;
    private CoordinatorBookRoomViewModel coordinatorBookRoom;
    private boolean isCoordinator;

    public TestCase1()
    {
    }

    @Test void scenario1()
    {
        precondition(false);
        step1();
        step2_1();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7();
        step89_1();
        postcondition("C02.08 2023-07-01 09:00-15:00 (Simon)");
    }

    @Test void scenario2()
    {
        precondition(false);
        step1();
        step2_2();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7();
        step89_2("Date and time interval must not be empty");
        postcondition(null);
    }

    @Test void scenario3()
    {
        precondition(false);
        step1();
        step2_3();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7();
        step89_2("Date and time interval must not be empty");
        postcondition(null);
    }

    @Test void scenario4()
    {
        precondition(false);
        step1();
        step2_4();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7();
        step89_2("Date and time interval must not be empty");
        postcondition(null);
    }

    @Test void scenario5()
    {
        precondition(false);
        step1();
        step2_5();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7();
        step89_2("Date and time interval must not be empty");
        postcondition(null);
    }

    @Test void scenario6()
    {
        precondition(false);
        step1();
        step2_6();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7();
        step89_2("Date and time interval must not be empty");
        postcondition(null);
    }

    @Test void scenario7()
    {
        precondition(false);
        step1();
        step2_1();
        step3_2();
        step4_1();
        step5_1();
        step6_1();
        step7();
        step89_2("Start time of booking must be before end time of booking");
        postcondition(null);
    }

    @Test void scenario8()
    {
        precondition(false);
        step1();
        step2_1();
        step3_3();
        step4_1();
        step5_1();
        step6_1();
        step7();
        step89_2("Date and time interval must not be empty");
        postcondition(null);
    }

    @Test void scenario9()
    {
        precondition(false);
        step1();
        step2_1();
        step3_4();
        step4_1();
        step5_1();
        step6_1();
        step7();
        step89_2("Date and time interval must not be empty");
        postcondition(null);
    }

    @Test void scenario10()
    {
        precondition(false);
        step1();
        step2_1();
        step3_1();
        step4_2();
        step5_1();
        step6_1();
        step7();
        step89_1();
        postcondition("C02.07 2023-07-01 09:00-15:00 (Simon)");
    }

    @Test void scenario11()
    {
        precondition(false);
        step1();
        step2_1();
        step3_1();
        step4_3();
        step5_1();
        step6_1();
        step7();
        step89_1();
        postcondition("C02.07 2023-07-01 09:00-15:00 (Simon)");
    }

    @Test void scenario12()
    {
        precondition(false);
        step1();
        step2_1();
        step3_1();
        step4_4();
        step5_1();
        step6_1();
        step7();
        step89_2("Max- and min capacity must be a number");
        postcondition(null);
    }

    @Test void scenario13()
    {
        precondition(false);
        step1();
        step2_1();
        step3_1();
        step4_5();
        step5_1();
        step6_1();
        step7();
        step89_1();
        postcondition("C02.07 2023-07-01 09:00-15:00 (Simon)");
    }

    @Test void scenario14()
    {
        precondition(false);
        step1();
        step2_1();
        step3_1();
        step4_1();
        step5_2();
        step6_1();
        step7();
        step89_1();
        postcondition("C02.08 2023-07-01 09:00-15:00 (Simon)");
    }

    @Test void scenario15()
    {
        precondition(false);
        step1();
        step2_1();
        step3_1();
        step4_1();
        step5_1();
        step6_2();
        step7();
        step89_1();
        postcondition("C02.08 2023-07-01 09:00-15:00 (Simon)");
    }

    @Test void scenario16()
    {
        precondition(true);
        step1();
        step2_1();
        step10_1();
        step11_1();
        step12_1();
        step7();
        step89_1();
        postcondition("A02.01 2023-07-01 12:45-16:05 (Gitte)");
    }

    @Test void scenario17()
    {
        precondition(true);
        step1();
        step2_1();
        step10_2();
        step11_1();
        step12_1();
        step7();
        step89_2("Date and time interval must not be empty");
        postcondition(null);
    }

    @Test void scenario18()
    {
        precondition(true);
        step1();
        step2_1();
        step10_3();
        step11_1();
        step12_1();
        step7();
        step89_1();
        postcondition("A02.01 2023-07-01 08:20-11:50 (Gitte)");
    }

    @Test void scenario19()
    {
        precondition(true);
        step1();
        step2_1();
        step10_1();
        step11_2();
        step12_1();
        step7();
        step89_2("Date and time interval must not be empty");
        postcondition(null);
    }

    @Test void scenario20()
    {
        precondition(true);
        step1();
        step2_1();
        step10_1();
        step11_1();
        step12_2();
        step7();
        step89_2("Date and time interval must not be empty");
        postcondition(null);
    }

    private void precondition(boolean isCoordinator)
    {
        this.isCoordinator = isCoordinator;

        if(isCoordinator)
        {
            loginAs(VIAID_GITTE);
        }
        else
        {
            loginAs(VIAID_SIMON);
        }

    }

    private void step1()
    {
        userBookRoom = viewModelFactory.getUserBookRoomViewModel(viewHandler, client);
        coordinatorBookRoom = viewModelFactory.getCoordinatorBookRoomViewModel(viewHandler, client);
    }

    // NOTE: Fordi binding af type LocalDate, kan vi ikke simulere at brugeren indtaster tekst i viewmodel,
    // da det er javafx's DatePicker som parser brugerens input.
    private void step2_1() { userBookRoom.selectedDateProperty().set(LocalDate.of(2023, 7, 1));
                             coordinatorBookRoom.selectedStartDateProperty().set(LocalDate.of(2023, 7, 1)); }

    private void step2_2() { userBookRoom.selectedDateProperty().set(null);
                             coordinatorBookRoom.selectedStartDateProperty().set(null); }
    private void step2_3() { step2_2(); }
    private void step2_4() { step2_2(); }
    private void step2_5() { step2_2(); }
    private void step2_6() { step2_2(); }

    private void step3_1() { userBookRoom.selectedFromTimeProperty().set("09:00");  userBookRoom.selectedToTimeProperty().set("15:00"); coordinatorBookRoom.selectedFromTimeProperty().set("09:00");    coordinatorBookRoom.selectedToTimeProperty().set("15:00"); }
    private void step3_2() { userBookRoom.selectedFromTimeProperty().set("15:00");  userBookRoom.selectedToTimeProperty().set("09:00"); coordinatorBookRoom.selectedFromTimeProperty().set("15:00");    coordinatorBookRoom.selectedToTimeProperty().set("09:00"); }
    private void step3_3() { userBookRoom.selectedFromTimeProperty().set("09:00");  userBookRoom.selectedToTimeProperty().set(null);    coordinatorBookRoom.selectedFromTimeProperty().set("09:00");    coordinatorBookRoom.selectedToTimeProperty().set(null);    }
    private void step3_4() { userBookRoom.selectedFromTimeProperty().set(null);     userBookRoom.selectedToTimeProperty().set(null);    coordinatorBookRoom.selectedFromTimeProperty().set(null);       coordinatorBookRoom.selectedToTimeProperty().set(null);    }

    private void step4_1() { userBookRoom.selectedMinCap().setValue("8");   userBookRoom.selectedMaxCap().setValue("8");    coordinatorBookRoom.selectedMinCapProperty().set("8");  coordinatorBookRoom.selectedMaxCapProperty().set("8"); }
    private void step4_2() { userBookRoom.selectedMinCap().setValue("7");   userBookRoom.selectedMaxCap().setValue("9");    coordinatorBookRoom.selectedMinCapProperty().set("7");  coordinatorBookRoom.selectedMaxCapProperty().set("9"); }
    private void step4_3() { userBookRoom.selectedMinCap().setValue("");    userBookRoom.selectedMaxCap().setValue("9");    coordinatorBookRoom.selectedMinCapProperty().set("");   coordinatorBookRoom.selectedMaxCapProperty().set("9"); }
    private void step4_4() { userBookRoom.selectedMinCap().setValue("to");  userBookRoom.selectedMaxCap().setValue("");     coordinatorBookRoom.selectedMinCapProperty().set("to"); coordinatorBookRoom.selectedMaxCapProperty().set("");  }
    private void step4_5() { userBookRoom.selectedMinCap().setValue("");    userBookRoom.selectedMaxCap().setValue("");     coordinatorBookRoom.selectedMinCapProperty().set("");   coordinatorBookRoom.selectedMaxCapProperty().set("");  }

    private void step5_1() { userBookRoom.selectedBuildingProperty().set('C');  coordinatorBookRoom.selectedBuildingProperty().set('C'); }
    private void step5_2() { userBookRoom.selectedBuildingProperty().set(null); coordinatorBookRoom.selectedBuildingProperty().set(null); }

    private void step6_1() { userBookRoom.selectedFloorProperty().set(2);    coordinatorBookRoom.selectedFloorProperty().set(2);    }
    private void step6_2() { userBookRoom.selectedFloorProperty().set(null); coordinatorBookRoom.selectedFloorProperty().set(null); }

    private void step7() { userBookRoom.showAvailableRooms(); coordinatorBookRoom.showAvailableRooms(); }
    private void step89_1()
    {
        viewHandler.setOkCancelChoice(true); // Tryk bekræft booking

        if(isCoordinator)
        {
            coordinatorBookRoom.bookRoom(coordinatorBookRoom.getRoomList().get(0));
        }
        else
        {
            userBookRoom.bookRoom(userBookRoom.getRoomList().get(0));
        }
    }

    private void step89_2(String message)
    {
        assertEquals(viewHandler.getLatestDialog(), message);
    }

    private void step10_1()
    {
        coordinatorBookRoom.prefixCheckBoxProperty().setValue(true);
        coordinatorBookRoom.selectedPreFixTimeProperty().set(coordinatorBookRoom.getPreFixTimes().get(1));
    }

    private void step10_2()
    {
        coordinatorBookRoom.prefixCheckBoxProperty().setValue(true);
    }

    private void step10_3()
    {
        coordinatorBookRoom.prefixCheckBoxProperty().setValue(true);
        coordinatorBookRoom.selectedPreFixTimeProperty().set(coordinatorBookRoom.getPreFixTimes().get(0));
    }

    private void step11_1() { coordinatorBookRoom.selectedCategoryProperty().set(null); }
    private void step11_2() { coordinatorBookRoom.selectedCategoryProperty().set(coordinatorBookRoom.getColors().get(0)); }

    private void step12_1() { coordinatorBookRoom.selectedCourseProperty().set(null); }
    private void step12_2() { coordinatorBookRoom.selectedCourseProperty().set(coordinatorBookRoom.getCourses().get(0)); }

    private void postcondition(String bookingText)
    {
        try
        {
            List<Booking> bookings = server.database().getBookingsForUser(client.getUser(), LocalDate.of(2023, 7, 1), LocalDate.of(2023, 7, 1), null);
            if(bookingText == null)
            {
                assertEquals(bookings.size(), 0);
            }
            else
            {
                assertEquals(bookings.get(0).toString(), bookingText);
            }
        }
        catch (PersistenceException e)
        {
            throw new RuntimeException(e);
        }
    }
}
