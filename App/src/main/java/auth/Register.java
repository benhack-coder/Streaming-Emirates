package auth;

import request.HTTPRequestException;

public class Register extends Authentication {
    public Register(String password, String userName) {
        super(password, userName);
    }

    public int getRequestResult() throws HTTPRequestException {
        return makeHttpRequest("http://anonymouslore.tk:8080/user/register");
    }
}
