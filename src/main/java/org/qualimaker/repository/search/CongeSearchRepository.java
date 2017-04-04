package org.qualimaker.repository.search;

import org.qualimaker.domain.Conge;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Conge entity.
 */
public interface CongeSearchRepository extends ElasticsearchRepository<Conge, Long> {
}
