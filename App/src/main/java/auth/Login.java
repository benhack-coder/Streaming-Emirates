package auth;

import request.HTTPRequestException;

import java.io.IOException;

public class Login extends Authentication {

    private final String URL = "http://anonymouslore.tk:8080/user/login";

    public Login(String password, String userName) {
        super(password, userName);
    }

    /*
    getRequestResult() is overloaded in case the password is already hashed, so it won't be hashed twice
     */
    public int getRequestResult() throws HTTPRequestException {
        return makeHttpRequest(URL);
    }
    public int getRequestResult(String hashedPassword) throws HTTPRequestException {
        return makeHttpRequest(URL,  hashedPassword);
    }
}
