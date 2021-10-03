import auth.Login;
import auth.Register;
import config.AppConfigHelper;
import org.junit.jupiter.api.Test;
import request.HTTPRequestException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationTest {

    @Test
    void testIfUserCanRegister() throws IOException, HTTPRequestException {
        Register register = new Register("helloworld", "unittest4");
        int result = register.getRequestResult();
        assertEquals(1, result);
    }

    @Test
    void testIfUserCanLogin() throws IOException, HTTPRequestException {
        Login login = new Login("helloworld", "unittest2");
        int result = login.getRequestResult();
        boolean checkResult = (result != 0);
        assertTrue(checkResult);
    }

    @Test
    void testIfUserCannotLoginWithWrongCredentials() throws IOException, HTTPRequestException {
        Login login = new Login("12345678", "unittest2");
        int result = login.getRequestResult();
        boolean checkResult = (result == 0);
        assertTrue(checkResult);
    }
    @Test
    void testIfUserAlreadyExists() throws IOException, HTTPRequestException {
        Register register = new Register("blablablabla", "unittest1");
        int result = register.getRequestResult();
        assertEquals(0, result);
    }

    @Test
    void test() throws IOException, HTTPRequestException {
        String password = (String) AppConfigHelper.getConfigValue("password");
        String username = (String) AppConfigHelper.getConfigValue("username");
        Login login = new Login(password, username);
        int result = login.getRequestResult();
        boolean success = (result != 0);
        assertTrue(success);
    }
}