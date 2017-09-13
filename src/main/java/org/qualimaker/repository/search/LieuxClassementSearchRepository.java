package org.qualimaker.repository.search;

import org.qualimaker.domain.LieuxClassement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LieuxClassement entity.
 */
public interface LieuxClassementSearchRepository extends ElasticsearchRepository<LieuxClassement, Long> {
}
