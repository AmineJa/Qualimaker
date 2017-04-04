package org.qualimaker.repository.search;

import org.qualimaker.domain.Etat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Etat entity.
 */
public interface EtatSearchRepository extends ElasticsearchRepository<Etat, Long> {
}