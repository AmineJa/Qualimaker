package org.qualimaker.repository.search;

import org.qualimaker.domain.Calendrier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Calendrier entity.
 */
public interface CalendrierSearchRepository extends ElasticsearchRepository<Calendrier, Long> {
}
