package org.qualimaker.repository.search;

import org.qualimaker.domain.Evaluation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Evaluation entity.
 */
public interface EvaluationSearchRepository extends ElasticsearchRepository<Evaluation, Long> {
}
