package org.qualimaker.repository.search;

import org.qualimaker.domain.Programme;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Programme entity.
 */
public interface ProgrammeSearchRepository extends ElasticsearchRepository<Programme, Long> {
}
