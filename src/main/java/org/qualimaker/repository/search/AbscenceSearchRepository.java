package org.qualimaker.repository.search;

import org.qualimaker.domain.Abscence;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Abscence entity.
 */
public interface AbscenceSearchRepository extends ElasticsearchRepository<Abscence, Long> {
}
