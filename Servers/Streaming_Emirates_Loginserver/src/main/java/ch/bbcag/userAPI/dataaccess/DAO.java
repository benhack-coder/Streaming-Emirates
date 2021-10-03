package ch.bbcag.userAPI.dataaccess;

import ch.bbcag.userAPI.entity.User;
import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface DAO {

    public List<User> retrieveUser(String username);
    public boolean validateLogin(@RequestBody User user) throws JSONException;
    public boolean insertData(User user);
}
