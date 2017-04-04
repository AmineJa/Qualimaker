package org.qualimaker.repository.search;

import org.qualimaker.domain.TypeContrat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TypeContrat entity.
 */
public interface TypeContratSearchRepository extends ElasticsearchRepository<TypeContrat, Long> {
}
