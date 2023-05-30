package test;

import booking.client.model.ClientModelException;
import booking.client.viewModel.coordinatorGUIVM.AddRoomViewModel;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorHomeScreenViewModel;
import booking.shared.objects.RoomType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static test.TestConstants.*;

public class TestCase7 extends TestClientServerIntegrationBase
{
    private AddRoomViewModel addRoomViewModel;
    private CoordinatorHomeScreenViewModel homeScreenViewModel;

    // State som normalt er håndteret af controller
    private String name;
    private RoomType type;
    private String maxComf;
    private String maxSafety;
    private String size;
    private String comment;


    //
    // Scenarios
    //

    @Test void scenario1()
    {
        precondition();
        step1();
        step2_1();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step8_1();
        step9_1();
        postcondition(true);
    }

    @Test void scenario2()
    {
        precondition();
        step1();
        step2_2();
        step3_1();
        step4_2();
        step5_2();
        step6_2();
        step8_2();
        step9_1();
        postcondition(true);
    }

    @Test void scenario3()
    {
        precondition();
        step1();
        step2_3();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step8_3();
        step9_1();
        postcondition(true);
    }

    @Test void scenario4()
    {
        precondition();
        step1();
        step2_4();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step8_1();
        step9_2("Name, room type, max comfort, max safety or size must not be empty");
        postcondition(false);
    }

    @Test void scenario5()
    {
        precondition();
        step1();
        step2_1();
        step3_1();
        step4_3();
        step5_1();
        step6_1();
        step8_1();
        step9_2("Comfort capacity must not be negative");
        postcondition(false);
    }

    @Test void scenario6()
    {
        precondition();
        step1();
        step2_1();
        step3_1();
        step4_4(); // TODO(rune): Drop max på maxComf?
        step5_1();
        step6_1();
        step8_1();
        step9_2("");
        postcondition(false);
    }

    @Test void scenario7()
    {
        precondition();
        step1();
        step2_1();
        step3_1();
        step4_5();
        step5_1();
        step6_1();
        step8_1();
        step9_2("Max capacity and size must be numbers");
        postcondition(false);
    }

    @Test void scenario8()
    {
        precondition();
        step1();
        step2_1();
        step3_1();
        step4_6();
        step5_1();
        step6_1();
        step8_1();
        step9_2("Name, room type, max comfort, max safety or size must not be empty");
        postcondition(false);
    }

    @Test void scenario9()
    {
        precondition();
        step1();
        step2_1();
        step3_1();
        step4_1();
        step5_3();
        step6_1();
        step8_1();
        step9_2("Safety capacity must not be negative");
        postcondition(false);
    }

    @Test void scenario10()
    {
        precondition();
        step1();
        step2_1();
        step3_1();
        step4_1();
        step5_4(); // TODO(rune): Drop max på maxSafety?
        step6_1();
        step8_1();
        step9_2("");
        postcondition(false);
    }

    @Test void scenario11()
    {
        precondition();
        step1();
        step2_1();
        step3_1();
        step4_1();
        step5_5();
        step6_1();
        step8_1();
        step9_2("Max capacity and size must be numbers");
        postcondition(false);
    }

    @Test void scenario12()
    {
        precondition();
        step1();
        step2_1();
        step3_1();
        step4_1();
        step5_6();
        step6_1();
        step8_1();
        step9_2("Name, room type, max comfort, max safety or size must not be empty");
        postcondition(false);
    }

    @Test void scenario13()
    {
        precondition();
        step1();
        step2_1();
        step3_1();
        step4_1();
        step5_1();
        step6_3();
        step8_1();
        step9_2("Size must not be negative");
        postcondition(false);
    }

    @Test void scenario14()
    {
        precondition();
        step1();
        step2_1();
        step3_1();
        step4_1();
        step5_1();
        step6_4(); // TODO(rune): Drop max på size?
        step8_1();
        step9_2("");
        postcondition(false);
    }

    @Test void scenario15()
    {
        precondition();
        step1();
        step2_1();
        step3_1();
        step4_1();
        step5_1();
        step6_5();
        step8_1();
        step9_2("Max capacity and size must be numbers");
        postcondition(false);
    }

    @Test void scenario16()
    {
        precondition();
        step1();
        step2_1();
        step3_1();
        step4_1();
        step5_1();
        step6_6();
        step8_1();
        step9_2("Name, room type, max comfort, max safety or size must not be empty");
        postcondition(false);
    }

    @Test void scenario17()
    {
        precondition();
        step1();
        step2_1();
        step3_1();
        step4_1();
        step5_1();
        step6_6();
        step8_1();
        step9_2("Name, room type, max comfort, max safety or size must not be empty");
        postcondition(false);
    }

    @Test void scenario18()
    {
        precondition();
        step1();
        step2_1();
        step3_2();
        step4_1();
        step5_1();
        step6_1();
        step8_1();
        step9_2("Name, room type, max comfort, max safety or size must not be empty");
        postcondition(false);
    }

    @Test void scenario19()
    {
        precondition();
        step1();
        step2_4();
        // TODO(rune): Docs trin 3.6 er et spøgelse
        step4_6();
        step5_6();
        step6_6();
        step8_1();
        step9_2("Name, room type, max comfort, max safety or size must not be empty");
        postcondition(false);
    }

    //
    // Precondition
    //

    private void precondition()
    {
        loginAs(VIAID_GITTE);
        homeScreenViewModel = viewModelFactory.getCoordinatorHomeScreenViewModel(viewHandler, client);
    }

    //
    // Steps
    //

    private void step1()
    {
        homeScreenViewModel.changeToAddRoom();
        assertEquals(viewHandler.getLatestView(), AddRoomViewModel.class);
        addRoomViewModel = viewModelFactory.getAddRoomViewModel(viewHandler, client);
    }

    // TODO(rune): Test når room name eksisterer i forvejen.

    private void step2_1()  { name = "C03.05"; }
    private void step2_2()  { name = "abc"; }
    private void step2_3()  { name = "123"; }
    private void step2_4()  { name = ""; }

    private void step3_1() { type = addRoomViewModel.getRoomTypes().get(1); }
    private void step3_2() { type = null; }

    private void step4_1() { maxComf = "1"; }
    private void step4_2() { maxComf = "234"; }
    private void step4_3() { maxComf = "-1"; }
    private void step4_4() { maxComf = "123456"; }
    private void step4_5() { maxComf = "abc"; }
    private void step4_6() { maxComf = ""; }

    private void step5_1() { maxSafety = "1"; }
    private void step5_2() { maxSafety = "234"; }
    private void step5_3() { maxSafety = "-1"; }
    private void step5_4() { maxSafety = "123456"; }
    private void step5_5() { maxSafety = "abc"; }
    private void step5_6() { maxSafety = ""; }

    private void step6_1() { size = "1"; }
    private void step6_2() { size = "234"; }
    private void step6_3() { size = "-1"; }
    private void step6_4() { size = "123456"; }
    private void step6_5() { size = "abc"; }
    private void step6_6() { size = ""; }

    // TODO(rune): Ret docs trin 8, blank står der to gange både 8.1 og 8.4
    private void step8_1() { comment = ""; }
    private void step8_2() { comment = "abc"; }
    private void step8_3() { comment = "123"; }

    private void step9_1()
    {
        addRoomViewModel.createRoom(name, type, maxComf, maxSafety, size, comment, false, null);
        assertEquals(viewHandler.getLatestDialog(), null);
    }

    private void step9_2(String errorMessage)
    {
        addRoomViewModel.createRoom(name, type, maxComf, maxSafety, size, comment, false, null);
        assertEquals(viewHandler.getLatestDialog(), errorMessage);
    }

    //
    // Postcondition
    //

    private void postcondition(boolean isPostconditionAchieved)
    {
        try
        {
            if (isPostconditionAchieved)
            {
                assertNotEquals(client.getRoom(name), null);
            }
            else
            {
                assertEquals(client.getRoom(name), null);
            }
        }
        catch (ClientModelException e)
        {
            throw new RuntimeException(e);
        }
    }
}
