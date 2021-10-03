package ch.bbcag.YoutubeAPI.dataaccess;

import ch.bbcag.YoutubeAPI.entity.WatchedVideo;
import ch.bbcag.YoutubeAPI.repository.IdsRepository;

import java.util.List;

public interface DAO {

    int insertData(WatchedVideo video);
    List<IdsRepository> getVideoIds(WatchedVideo video);
}
