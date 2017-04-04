package org.qualimaker.repository.search;

import org.qualimaker.domain.Fichierjoint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Fichierjoint entity.
 */
public interface FichierjointSearchRepository extends ElasticsearchRepository<Fichierjoint, Long> {
}
