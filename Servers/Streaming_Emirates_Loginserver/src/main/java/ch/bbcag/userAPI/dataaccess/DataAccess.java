package ch.bbcag.userAPI.dataaccess;

import ch.bbcag.userAPI.entity.User;
import org.json.JSONException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
public class DataAccess implements DAO {

    private JdbcTemplate jdbcTemplate;

    public DataAccess(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<User> rowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setPassword(rs.getString("password"));
        return user;
    };

    @Override
    public List<User> retrieveUser(String username) {
        try {
            String query = "SELECT password FROM user WHERE username = '" + username + "'";
            return jdbcTemplate.query(query, rowMapper);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean validateLogin(@RequestBody User user) throws JSONException {
        List<User> users;
        try {
            users = retrieveUser(user.getUsername());
            return users.get(0).getPassword().equals(user.getPassword());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean insertData(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String query = " INSERT INTO user (username, password) VALUES (?,?);";
        try {
            jdbcTemplate.update(query, username, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
