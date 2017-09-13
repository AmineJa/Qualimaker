package org.qualimaker.repository.search;

import org.qualimaker.domain.Enregistrement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Enregistrement entity.
 */
public interface EnregistrementSearchRepository extends ElasticsearchRepository<Enregistrement, Long> {
}
