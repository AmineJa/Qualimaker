package org.qualimaker.repository.search;

import org.qualimaker.domain.Natureformation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Natureformation entity.
 */
public interface NatureformationSearchRepository extends ElasticsearchRepository<Natureformation, Long> {
}
