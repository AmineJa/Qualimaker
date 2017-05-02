package org.qualimaker.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Events entity.
 */
public interface EventsSearchRepository extends ElasticsearchRepository<Events, Long> {
}
