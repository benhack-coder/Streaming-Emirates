package ch.bbcag.YoutubeAPI.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class IdsRepository {

    public List<Ids> videoIds = new ArrayList<>();
}
