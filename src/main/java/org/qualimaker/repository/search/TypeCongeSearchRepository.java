package org.qualimaker.repository.search;

import org.qualimaker.domain.TypeConge;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TypeConge entity.
 */
public interface TypeCongeSearchRepository extends ElasticsearchRepository<TypeConge, Long> {
}
