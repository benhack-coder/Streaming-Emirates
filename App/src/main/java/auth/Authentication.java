package auth;

import common.PasswordHasher;
import entity.User;
import request.HTTPRequestException;
import request.IRequest;
import request.RequestType;
import request.RequestUtil;


/**
 * This class is an abstract class with the subclasses Login and Register
 * It makes the request to our Login-Server and returns the response to the subclasses
 * It reaches the given username and password to the superclass User, where this will be stored
 */

public abstract class Authentication extends User {
    private final RequestType requestType = RequestType.POST;

    public Authentication(String password, String userName) {
        super(userName, password);
    }

    private String getHashedPassword() {
        return PasswordHasher.getsha512(getPassword());
    }

    protected int makeHttpRequest(String url) throws HTTPRequestException {
        String json = "{ \"username\": \"" + getUserName() + "\", \"password\": \"" + getHashedPassword() + "\" }";
        IRequest request = new RequestUtil(url, json, requestType);
        return Integer.parseInt(request.makeRequest());
    }

    protected int makeHttpRequest(String url, String hashedPassword) throws HTTPRequestException {
        String json = "{ \"username\": \"" + getUserName() + "\", \"password\": \"" + hashedPassword + "\" }";
        IRequest request = new RequestUtil(url, json, requestType);
        return Integer.parseInt(request.makeRequest());
    }
}
