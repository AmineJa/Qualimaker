package org.qualimaker.repository.search;

import org.qualimaker.domain.DocumentInterne;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DocumentInterne entity.
 */
public interface DocumentInterneSearchRepository extends ElasticsearchRepository<DocumentInterne, Long> {
}
