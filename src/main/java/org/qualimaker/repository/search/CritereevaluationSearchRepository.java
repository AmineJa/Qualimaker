package org.qualimaker.repository.search;

import org.qualimaker.domain.Critereevaluation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Critereevaluation entity.
 */
public interface CritereevaluationSearchRepository extends ElasticsearchRepository<Critereevaluation, Long> {
}
