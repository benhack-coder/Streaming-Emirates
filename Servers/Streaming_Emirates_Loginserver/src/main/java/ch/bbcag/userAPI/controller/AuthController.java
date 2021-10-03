package ch.bbcag.userAPI.controller;


import ch.bbcag.userAPI.dataaccess.DAO;
import ch.bbcag.userAPI.entity.User;
import ch.bbcag.userAPI.request.IRequest;
import ch.bbcag.userAPI.request.RequestType;
import ch.bbcag.userAPI.request.RequestUtil;
import org.json.JSONException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Random;

@RequestMapping("/user")
@RestController
public class AuthController {

    private DAO dao;

    public AuthController(DAO dao) {
        this.dao = dao;
    }

    @PostMapping("/login")
    public int loginValidator(@RequestBody User user) throws JSONException, IOException {
        if (dao.validateLogin(user)) {
            int sessionId = generateRandomNum();
            Thread thread = new Thread() {
                public void run() {
                    try {
                        makeRequest(user.getUsername(), sessionId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
            return sessionId;
        } else {
            return 0;
        }
    }

    @PostMapping("/register")
    public int register(@RequestBody User user) throws IOException {
        if (dao.insertData(user)) {
            return 1;
        }else {
            return 0;
        }
    }

    private void makeRequest(String username, int sessionId) throws IOException {
        String url = "http://anonymouslore.tk:8081/history";
        String json = "{\"username\": \"" + username +"\", \"sessionId\": \"" + sessionId + "\" }";
        IRequest request = new RequestUtil(url, json, RequestType.POST);
        request.makeRequest();
    }

    private int generateRandomNum() {
        Random myRandom = new Random();
        return myRandom.nextInt(1000);
    }
}

