package ch.bbcag.YoutubeAPI.dataaccess;

import ch.bbcag.YoutubeAPI.entity.WatchedVideo;
import ch.bbcag.YoutubeAPI.repository.Ids;
import ch.bbcag.YoutubeAPI.repository.IdsRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class DataAccess implements DAO {

    private final JdbcTemplate jdbcTemplate;
    private IdsRepository repository;

    public DataAccess(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Integer> rowMapperKeys = (rs, rowNum) -> rs.getInt("id");

    RowMapper <IdsRepository> rowMapperIds = (rs, rowNum) -> {
        Ids id = new Ids();
        id.setId(rs.getString("video_id"));
        repository.videoIds.add(id);
        return repository;
    };

    @Override
    public int insertData(WatchedVideo video) {
        String userName = video.getUsername();
        int userId = Objects.requireNonNull(getForeignKey(userName)).get(0);
        String videoId = video.getVideoId();
        String query = "INSERT INTO history (user_id, video_id) VALUES (?,?);";
        try {
            jdbcTemplate.update(query, userId, videoId);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public List<IdsRepository> getVideoIds(WatchedVideo video) {
        this.repository = new IdsRepository();
        try {
            String username = video.getUsername();
            int userId = Objects.requireNonNull(getForeignKey(username)).get(0);
            String query = "SELECT video_id FROM history WHERE user_id = '" + userId + "';";
            return jdbcTemplate.query(query, rowMapperIds);
        } catch(Exception e){
            return null;
        }
    }

    private List<Integer> getForeignKey(String username) {
       try {
           String query = "SELECT id FROM user WHERE username = '" + username + "';";
           return jdbcTemplate.query(query, rowMapperKeys);
       } catch (Exception e) {
           return null;
       }
    }
}
