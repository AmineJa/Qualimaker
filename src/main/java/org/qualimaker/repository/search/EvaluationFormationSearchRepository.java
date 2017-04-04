package org.qualimaker.repository.search;

import org.qualimaker.domain.EvaluationFormation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EvaluationFormation entity.
 */
public interface EvaluationFormationSearchRepository extends ElasticsearchRepository<EvaluationFormation, Long> {
}
