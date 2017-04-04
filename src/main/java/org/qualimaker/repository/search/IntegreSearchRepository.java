package org.qualimaker.repository.search;

import org.qualimaker.domain.Integre;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Integre entity.
 */
public interface IntegreSearchRepository extends ElasticsearchRepository<Integre, Long> {
}
