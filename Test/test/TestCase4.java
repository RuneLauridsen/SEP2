package test;

import booking.client.viewModel.loginVM.LoginViewModel;
import booking.client.viewModel.loginVM.RegisterViewModel;
import booking.server.persistene.PersistenceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCase4 extends TestClientServerIntegrationBase
{
    private RegisterViewModel registerViewModel;
    private LoginViewModel loginViewModel;

    //
    // Scenarios
    //

    @Test void scenario1()
    {
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_1();
        step7_1();
        postcondition(true);
    }

    @Test void scenario2()
    {
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_3();
        step7_1();
        postcondition(true);
    }

    @Test void scenario3()
    {
        step1();
        step2();
        step3_2();
        step4_1();
        step5_1();
        step6_1();
        step7_2("VIA ID must be 6 digits");
        postcondition(false);
    }

    @Test void scenario4()
    {
        step1();
        step2();
        step3_3();
        step4_1();
        step5_1();
        step6_1();
        step7_2("VIA ID must be 6 digits");
        postcondition(false);
    }

    @Test void scenario5()
    {
        step1();
        step2();
        step3_4();
        step4_1();
        step5_1();
        step6_1();
        step7_2("VIA ID must be 6 digits");
        postcondition(false);
    }

    @Test void scenario6()
    {
        step1();
        step2();
        step3_1();
        step4_2();
        step5_1();
        step6_1();
        step7_1();
        postcondition(true);
    }

    @Test void scenario7()
    {
        step1();
        step2();
        step3_1();
        step4_3();
        step5_1();
        step6_1();
        step7_2("Invalid username");
        postcondition(false);
    }

    @Test void scenario8()
    {
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_2();
        step7_1();
        postcondition(true);
    }

    @Test void scenario9()
    {
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_3();
        step7_1();
        postcondition(true);
    }

    @Test void scenario10()
    {
        step1();
        step2();
        step3_1();
        step4_1();
        step5_1();
        step6_4();
        step7_1();
        postcondition(true);
    }

    @Test void scenario11()
    {
        step1();
        step2();
        step3_1();
        step4_1();
        step5_2();
        step6_1();
        step7_2("No occupation selected");
        postcondition(false);
    }

    @Test void scenario12()
    {
        step1();
        step2();
        step3_5();
        step4_4();
        step5_2();
        step6_5();
        step7_2("VIA ID must be 6 digits");
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

    private void step2()
    {
        loginViewModel.showRegister();
        assertEquals(viewHandler.getLatestView(), RegisterViewModel.class);
        registerViewModel = viewModelFactory.getRegisterViewModel(viewHandler, client);
    }

    //
    // Step 3
    //

    private void step3_1()
    {
        registerViewModel.viaIDProperty().setValue("888888");
    }

    private void step3_2()
    {
        registerViewModel.viaIDProperty().setValue("123");
    }

    private void step3_3()
    {
        registerViewModel.viaIDProperty().setValue("abc");
    }

    private void step3_4()
    {
        registerViewModel.viaIDProperty().setValue("+123456");
    }

    private void step3_5()
    {
        registerViewModel.viaIDProperty().setValue("");
    }

    //
    // Step 4
    //

    private void step4_1()
    {
        registerViewModel.usernameProperty().setValue("Martin");
    }

    private void step4_2()
    {
        registerViewModel.usernameProperty().setValue("Ma-rtin");
    }

    private void step4_3()
    {
        registerViewModel.usernameProperty().setValue("1234");
    }

    private void step4_4()
    {
        registerViewModel.usernameProperty().setValue("");
    }

    //
    // Step 5
    //

    private void step5_1()
    {
        registerViewModel.userTypeProperty().setValue(registerViewModel.getUserTypes().get(1));
    }

    private void step5_2()
    {
        registerViewModel.userTypeProperty().setValue(null);
    }

    //
    // Step 6
    //

    private void step6_1()
    {
        registerViewModel.passwordProperty().setValue("9876");
    }

    private void step6_2()
    {
        registerViewModel.passwordProperty().setValue("abc");
    }

    private void step6_3()
    {
        registerViewModel.passwordProperty().setValue("abc123");
    }

    private void step6_4()
    {
        registerViewModel.passwordProperty().setValue("+1234");
    }

    private void step6_5()
    {
        registerViewModel.passwordProperty().setValue("");
    }

    //
    // Step 7
    //

    private void step7_1()
    {
        registerViewModel.register();
        assertEquals(viewHandler.getLatestDialog(), null);
    }

    private void step7_2(String message)
    {
        registerViewModel.register();
        assertEquals(viewHandler.getLatestDialog(), message);
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
                assertTrue(server.database().getUser(888888) != null);
            }
            else
            {
                assertTrue(server.database().getUser(888888) == null);
            }
        }
        catch (PersistenceException e)
        {
            throw new RuntimeException(e);
        }
    }
}
