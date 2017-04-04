package org.qualimaker.repository.search;

import org.qualimaker.domain.Sites;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Sites entity.
 */
public interface SitesSearchRepository extends ElasticsearchRepository<Sites, Long> {
}
