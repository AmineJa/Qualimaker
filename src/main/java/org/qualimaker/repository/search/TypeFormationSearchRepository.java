package org.qualimaker.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TypeFormation entity.
 */
public interface TypeFormationSearchRepository extends ElasticsearchRepository<TypeFormation, Long> {
}
