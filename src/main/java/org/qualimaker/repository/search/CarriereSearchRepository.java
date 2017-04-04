package org.qualimaker.repository.search;

import org.qualimaker.domain.Carriere;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Carriere entity.
 */
public interface CarriereSearchRepository extends ElasticsearchRepository<Carriere, Long> {
}
