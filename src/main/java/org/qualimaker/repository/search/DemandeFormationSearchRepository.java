package org.qualimaker.repository.search;

import org.qualimaker.domain.DemandeFormation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DemandeFormation entity.
 */
public interface DemandeFormationSearchRepository extends ElasticsearchRepository<DemandeFormation, Long> {
}
