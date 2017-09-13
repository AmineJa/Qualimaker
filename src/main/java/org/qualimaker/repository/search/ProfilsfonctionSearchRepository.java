package org.qualimaker.repository.search;

import org.qualimaker.domain.Profilsfonction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Profilsfonction entity.
 */
public interface ProfilsfonctionSearchRepository extends ElasticsearchRepository<Profilsfonction, Long> {
}
