package test;

import booking.client.model.ClientModelException;
import booking.client.viewModel.coordinatorGUIVM.AddRoomViewModel;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorHomeScreenViewModel;
import booking.client.viewModel.coordinatorGUIVM.EditRoomViewModel;
import booking.client.viewModel.sharedVM.PredefinedColor;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static test.TestConstants.VIAID_GITTE;

public class TestCase7 extends TestClientServerIntegrationBase
{
    private EditRoomViewModel editRoomViewModel;
    private CoordinatorHomeScreenViewModel homeScreenViewModel;

    // State som normalt er håndteret af controller
    private String name;
    private RoomType type;
    private String maxComf;
    private String maxSafety;
    private String size;
    private String comment;
    private String userComment;
    private PredefinedColor userColor;


    //
    // Scenarios
    //

    @Test void scenario1()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7_1();

        step9_1();
        step10_1();
        step11_1();
        step12_1a();
        postcondition(true);
    }

    @Test void scenario2()
    {
        precondition();
        step1();
        step2();
        step3_2();
        step4_1();
        step5_1();
        step6_1();
        step7_1();

        step9_1();
        step10_1();
        step11_1();
        step12_1a();
        postcondition(true);
    }

    @Test void scenario3()
    {
        precondition();
        step1();
        step2();
        step3_4();
        step4_1();
        step5_1();
        step6_1();
        step7_1();

        step9_1();
        step10_1();
        step11_1();
        step12_2a("Name, room type, max comfort, max safety or size must not be empty");
        postcondition(false);
    }

    @Test void scenario4()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_3();
        step6_1();
        step7_1();

        step9_1();
        step10_1();
        step11_1();
        step12_2a("Comfort capacity must not be negative");
        postcondition(false);
    }

    @Test void scenario5()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_2();
        step6_1();
        step7_1();

        step9_1();
        step10_1();
        step11_1();
        step12_2a("Max capacity and size must be numbers");
        postcondition(false);
    }

    @Test void scenario6()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_3();
        step6_1();
        step7_1();

        step9_1();
        step10_1();
        step11_1();
        step12_2a("Comfort capacity must not be negative");
        postcondition(false);
    }

    @Test void scenario7()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_4();
        step6_1();
        step7_1();

        step9_1();
        step10_1();
        step11_1();
        step12_2a("Name, room type, max comfort, max safety or size must not be empty");
        postcondition(false);
    }

    @Test void scenario8()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_2();
        step7_2();

        step9_1();
        step10_1();
        step11_1();
        step12_2a("Max capacity and size must be numbers");
        postcondition(false);
    }

    @Test void scenario9()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_3();
        step7_1();

        step9_1();
        step10_1();
        step11_1();
        step12_2a("Safety capacity must not be negative");
        postcondition(false);
    }

    @Test void scenario10()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_4();
        step7_1();

        step9_1();
        step10_1();
        step11_1();
        step12_2a("Name, room type, max comfort, max safety or size must not be empty");
        postcondition(false);
    }

    @Test void scenario11()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7_2();

        step9_1();
        step10_1();
        step11_1();
        step12_2a("Max capacity and size must be numbers");
        postcondition(false);
    }

    @Test void scenario12()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7_3();

        step9_1();
        step10_1();
        step11_1();
        step12_2a("Size must not be negative");
        postcondition(false);
    }

    @Test void scenario13()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7_4();

        step9_1();
        step10_1();
        step11_1();
        step12_2a("Name, room type, max comfort, max safety or size must not be empty");
        postcondition(false);
    }

    @Test void scenario14()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7_1();

        step9_2();
        step10_1();
        step11_1();
        step12_1a();
        postcondition(true);
    }

    @Test void scenario15()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7_1();

        step9_3();
        step10_1();
        step11_1();
        step12_1a();
        postcondition(true);
    }

    @Test void scenario16()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7_1();

        step9_4();
        step10_1();
        step11_1();
        step12_1a();
        postcondition(true);
    }

    @Test void scenario17()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7_1();

        step9_1();
        step10_2();
        step11_1();
        step12_1a();
        postcondition(true);
    }

    @Test void scenario18()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7_1();

        step9_1();
        step10_3();
        step12_1a();
        postcondition(true);
    }

    @Test void scenario19()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7_1();

        step9_1();
        step10_4();
        step11_1();
        step12_1a();
        postcondition(true);
    }

    @Test void scenario20()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7_1();

        step9_1();
        step10_1();
        step11_2();
        step12_1a();
        postcondition(true);
    }

    @Test void scenario21()
    {
        precondition();
        step1();
        step2();
        step3_4();
        step4_1();
        step5_4();
        step6_4();
        step7_4();

        step9_4();
        step10_4();
        step11_2();
        step12_2a("Name, room type, max comfort, max safety or size must not be empty");
        postcondition(false);
    }

    @Test void scenario22()
    {
        precondition();
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7_1();

        step9_1();
        step10_1();
        step11_1();
        step12_b();
        postcondition(true);
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
        homeScreenViewModel.changeToEditRoom(homeScreenViewModel.getAllRooms().get(0));

    }

    private void step2()
    {
        assertEquals(viewHandler.getLatestView(), EditRoomViewModel.class);
        editRoomViewModel = viewModelFactory.getEditRoomViewModel(viewHandler, client, homeScreenViewModel.getAllRooms().get(0));
    }

    // TODO(rune): Test når room name eksisterer i forvejen.

    private void step3_1()  { name = "C02.14"; }
    private void step3_2()  { name = "abc"; }
    private void step3_3()  { name = "123"; }
    private void step3_4()  { name = ""; }

    private void step4_1() { type = editRoomViewModel.getRoomTypes().get(0); }

    private void step5_1() { maxComf = "12"; }
    private void step5_2() { maxComf = "abc"; }
    private void step5_3() { maxComf = "-13"; }
    private void step5_4() { maxComf = ""; }

    private void step6_1() { maxSafety = "12"; }
    private void step6_2() { maxSafety = "abc"; }
    private void step6_3() { maxSafety = "-13"; }
    private void step6_4() { maxSafety = ""; }

    private void step7_1() { size = "12"; }
    private void step7_2() { size = "abc"; }
    private void step7_3() { size = "-13"; }
    private void step7_4() { size = ""; }

    private void step9_1() { comment = "ABC!!"; }
    private void step9_2() { comment = "abc"; }
    private void step9_3() { comment = "123"; }
    private void step9_4() { comment = ""; }

    private void step10_1() { userComment = "ABC!!"; }
    private void step10_2() { userComment = "abc"; }
    private void step10_3() { userComment = "123"; }
    private void step10_4() { userComment = ""; }

    private void step11_1() { userColor = PredefinedColor.getPredefinedColors()[1]; }
    private void step11_2() { userColor = null; }

    private void step12_1a()
    {
        editRoomViewModel.updateRoom(name, type, maxComf, maxSafety, size, comment, false, null, userComment, userColor);
        assertEquals(viewHandler.getLatestDialog(), null);
    }

    private void step12_2a(String message)
    {
        editRoomViewModel.updateRoom(name, type, maxComf, maxSafety, size, comment, false, null, userComment, userColor);
        assertEquals(viewHandler.getLatestDialog(), message);
    }

    private void step12_b()
    {
        // Cancel sker i controlleren
    }

    //
    // Postcondition
    //

    private void postcondition(boolean isPostconditionAchieved)
    {
        try
        {
            Room room = client.getRoom(name);

            if (isPostconditionAchieved)
            {
                assertEquals(name, room.getName());
                assertEquals(type, room.getType());
                assertEquals(maxComf, room.getComfortCapacity() + "");
                assertEquals(maxSafety, room.getFireCapacity() + "");
                assertEquals(size, room.getSize() + "");
                assertEquals(comment, room.getComment());
                assertEquals(userComment, room.getUserComment());
                assertEquals(userColor == null ? -1 : userColor.getArgb(), room.getUserColor());
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
