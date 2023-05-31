package test;

import booking.client.viewModel.loginVM.LoginViewModel;
import org.junit.jupiter.api.Test;

import static booking.shared.socketMessages.ErrorResponseReason.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCase3 extends TestClientServerIntegrationBase
{
    private LoginViewModel loginViewModel;

    @Test void scenario1()
    {
        step1();
        step2_1();
        step3_1();
        step4_1();
        postcondition(true);
    }

    @Test void scenario2()
    {
        step1();
        step2_1();
        step3_2();
        step4_2(ERROR_RESPONSE_REASON_INVALID_CREDENTIALS.getMessage());
        postcondition(false);
    }

    @Test void scenario3()
    {
        step1();
        step2_2();
        step3_1();
        step4_2(ERROR_RESPONSE_REASON_INVALID_CREDENTIALS.getMessage());
        postcondition(false);
    }

    @Test void scenario4()
    {
        step1();
        step2_1();
        step3_5();
        step4_2(ERROR_RESPONSE_REASON_INVALID_CREDENTIALS.getMessage());
        postcondition(false);
    }

    @Test void scenario5()
    {
        step1();
        step2_5();
        step3_1();
        step4_2("VIA ID must be a number");
        postcondition(false);
    }

    @Test void scenario6()
    {
        step1();
        step2_5();
        step3_5();
        step4_2("VIA ID must be a number");
        postcondition(false);
    }

    @Test void scenario7()
    {
        step1();
        step2_5();
        step3_5();
        step4_2("VIA ID must be a number");
        postcondition(false);
    }

    @Test void scenario8()
    {
        step1();
        step2_1();
        step3_3();
        step4_2(ERROR_RESPONSE_REASON_INVALID_CREDENTIALS.getMessage());
        postcondition(false);
    }

    @Test void scenario9()
    {
        step1();
        step2_1();
        step3_4();
        step4_2(ERROR_RESPONSE_REASON_INVALID_CREDENTIALS.getMessage());
        postcondition(false);
    }

    @Test void scenario10()
    {
        step1();
        step2_3();
        step3_1();
        step4_2("VIA ID must be a number");
        postcondition(false);
    }

    @Test void scenario11()
    {
        step1();
        step2_4();
        step3_1();
        step4_2(ERROR_RESPONSE_REASON_INVALID_CREDENTIALS.getMessage());
        postcondition(false);
    }

    //
    // Step 1
    //

    private void step1()
    {
        loginViewModel = viewModelFactory.getLoginViewModel(viewHandler, client);
    }

    //
    // Step 2
    //

    private void step2_1()
    {
        loginViewModel.VIAIDProperty().set("555555");
    }

    private void step2_2()
    {
        loginViewModel.VIAIDProperty().set("123");
    }

    private void step2_3()
    {
        loginViewModel.VIAIDProperty().set("abc");
    }

    private void step2_4()
    {
        loginViewModel.VIAIDProperty().set("+123456");
    }

    private void step2_5()
    {
        loginViewModel.VIAIDProperty().set("");
    }

    //
    // Step 3
    //

    private void step3_1()
    {
        loginViewModel.passwordProperty().set("1234");
    }

    private void step3_2()
    {
        loginViewModel.passwordProperty().set("123");
    }

    private void step3_3()
    {
        loginViewModel.passwordProperty().set("abc");
    }

    private void step3_4()
    {
        loginViewModel.passwordProperty().set("+123456");
    }

    private void step3_5()
    {
        loginViewModel.passwordProperty().set("");
    }

    //
    // Step 4
    //

    private void step4_1()
    {
        loginViewModel.loginAction();
        assertEquals(viewHandler.getLatestDialog(), null);
    }

    private void step4_2(String dialogMessage)
    {
        loginViewModel.loginAction();
        assertEquals(viewHandler.getLatestDialog(), dialogMessage);
    }

    //
    // Postcondition
    //

    private void postcondition(boolean isLoggedIn)
    {
        if(isLoggedIn)
        {
            assertTrue(client.getUser() != null);
        }
        else
        {
            assertTrue(client.getUser() == null);
        }
    }
}
