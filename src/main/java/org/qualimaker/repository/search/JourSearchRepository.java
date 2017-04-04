package org.qualimaker.repository.search;

import org.qualimaker.domain.Jour;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Jour entity.
 */
public interface JourSearchRepository extends ElasticsearchRepository<Jour, Long> {
}
