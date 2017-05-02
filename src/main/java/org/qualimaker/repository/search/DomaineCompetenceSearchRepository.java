package org.qualimaker.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DomaineCompetence entity.
 */
public interface DomaineCompetenceSearchRepository extends ElasticsearchRepository<DomaineCompetence, Long> {
}
