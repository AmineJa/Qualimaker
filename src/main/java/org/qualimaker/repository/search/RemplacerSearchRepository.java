package org.qualimaker.repository.search;

import org.qualimaker.domain.Remplacer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Remplacer entity.
 */
public interface RemplacerSearchRepository extends ElasticsearchRepository<Remplacer, Long> {
}
