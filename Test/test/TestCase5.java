package test;

import booking.client.viewModel.roomInfoVM.RoomInfoViewModel;
import booking.client.viewModel.userGUIVM.UserHomeScreenViewModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static test.TestConstants.*;

public class TestCase5 extends TestClientServerIntegrationBase
{
    private UserHomeScreenViewModel homeScreenViewModel;

    //
    // Scenarios
    //

    @Test void scenario1()
    {
        precondition();
        step1_1();
        step2();
        postcondition(true);
    }

    @Test void scenario2()
    {
        precondition();
        step1_2();
        step2();
        postcondition(true);
    }

    @Test void scenario3()
    {
        precondition();
        step1_3();
        step2();

        // TODO(rune)
    }

    @Test void scenario4()
    {
        precondition();
        step1_3();

        // TODO(rune)
    }

    @Test void scenario5()
    {
        precondition();
        step1_5();

        assertEquals(homeScreenViewModel.searchDisable().get(), true);
    }



    //
    // Pre condition
    //

    private void precondition()
    {
        loginAs(VIAID_SIMON);
        homeScreenViewModel = viewModelFactory.getUserHomeScreenViewModel(viewHandler, client);
    }

    //
    // Step 1
    //

    // TODO(rune): Ændr docs C02.06 -> CS02.07

    private void step1_1()
    {
        homeScreenViewModel.getSearchProperty().set("C02.07");
    }

    private void step1_2()
    {
        homeScreenViewModel.getSearchProperty().set("c02.07");
    }

    private void step1_3()
    {
        homeScreenViewModel.getSearchProperty().set("02.07");
    }

    private void step1_4()
    {
        homeScreenViewModel.getSearchProperty().set("c 02.07");
    }

    private void step1_5()
    {
        homeScreenViewModel.getSearchProperty().set("");
    }

    //
    // Step 2
    //

    private void step2()
    {
        homeScreenViewModel.changeToSearch();
    }

    //
    // Postcondition
    //

    private void postcondition(boolean isPostconditionAchieved)
    {
        if(isPostconditionAchieved)
        {
            assertEquals(viewHandler.getLatestView(), RoomInfoViewModel.class);
        }
        else
        {
            assertNotEquals(viewHandler.getLatestView(), RoomInfoViewModel.class);
        }
    }

    private void postcondition(String errorMessage)
    {
        assertEquals(viewHandler.getLatestDialog(), errorMessage);
    }
}
