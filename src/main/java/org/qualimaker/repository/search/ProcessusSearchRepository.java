package org.qualimaker.repository.search;

import org.qualimaker.domain.Processus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Processus entity.
 */
public interface ProcessusSearchRepository extends ElasticsearchRepository<Processus, Long> {
}
