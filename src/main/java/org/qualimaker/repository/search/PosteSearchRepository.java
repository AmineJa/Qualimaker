package org.qualimaker.repository.search;

import org.qualimaker.domain.Poste;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Poste entity.
 */
public interface PosteSearchRepository extends ElasticsearchRepository<Poste, Long> {
}
