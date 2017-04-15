package org.qualimaker.repository.search;

import org.qualimaker.domain.EtatDemande;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EtatDemande entity.
 */
public interface EtatDemandeSearchRepository extends ElasticsearchRepository<EtatDemande, Long> {
}
