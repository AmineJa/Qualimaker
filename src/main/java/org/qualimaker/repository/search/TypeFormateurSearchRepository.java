package org.qualimaker.repository.search;

import org.qualimaker.domain.TypeFormateur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TypeFormateur entity.
 */
public interface TypeFormateurSearchRepository extends ElasticsearchRepository<TypeFormateur, Long> {
}
