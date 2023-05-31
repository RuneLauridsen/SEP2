package test;

import booking.client.model.ClientModelException;
import booking.client.viewModel.coordinatorGUIVM.CoordinatorHomeScreenViewModel;
import booking.client.viewModel.coordinatorGUIVM.EditRoomViewModel;
import booking.shared.objects.Room;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static test.TestConstants.VIAID_GITTE;

public class TestCase8 extends TestClientServerIntegrationBase
{
    private CoordinatorHomeScreenViewModel homeScreen;
    private EditRoomViewModel editRoom;

    @Test void scenario1()
    {
        precondition();
        step1();
        step2();
        step3_1();
        postcondition(true);
    }

    @Test void scenario2()
    {
        precondition();
        step1();
        step2();
        step3_2();
        postcondition(false);
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
        Room room = homeScreen.getAllRooms().get(0);

        homeScreen.changeToEditRoom(room);
        assertEquals(viewHandler.getLatestView(), EditRoomViewModel.class);

        editRoom = viewModelFactory.getEditRoomViewModel(viewHandler, client, room);
    }

    private void step3_1()
    {
        viewHandler.setOkCancelChoice(true);
        editRoom.deleteRoom();
    }

    private void step3_2()
    {
        viewHandler.setOkCancelChoice(false);
        editRoom.deleteRoom();
    }

    private void postcondition(boolean isPostconditionAchieved)
    {
        try
        {
            if (isPostconditionAchieved)
            {
                assertEquals(client.getRoom("A02.01"), null);
            }
            else
            {
                assertNotEquals(client.getRoom("A02.01"), null);
            }
        }
        catch (ClientModelException e)
        {
            throw new RuntimeException(e);
        }
    }


}
