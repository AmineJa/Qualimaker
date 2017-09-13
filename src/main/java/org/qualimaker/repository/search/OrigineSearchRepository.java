package org.qualimaker.repository.search;

import org.qualimaker.domain.Origine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Origine entity.
 */
public interface OrigineSearchRepository extends ElasticsearchRepository<Origine, Long> {
}
