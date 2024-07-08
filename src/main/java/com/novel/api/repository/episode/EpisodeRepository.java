package com.novel.api.repository.episode;

import com.novel.api.domain.episode.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeRepository extends JpaRepository<Episode, Long>, EpisodeRepositoryCustom {


}
