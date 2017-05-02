package org.qualimaker.repository.search;

import org.qualimaker.domain.Servicepost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Servicepost entity.
 */
public interface ServicepostSearchRepository extends ElasticsearchRepository<Servicepost, Long> {
}
