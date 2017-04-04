package org.qualimaker.repository.search;

import org.qualimaker.domain.Serviice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Serviice entity.
 */
public interface ServiiceSearchRepository extends ElasticsearchRepository<Serviice, Long> {
}
