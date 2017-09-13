package org.qualimaker.repository.search;

import org.qualimaker.domain.DocumenExterne;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DocumenExterne entity.
 */
public interface DocumenExterneSearchRepository extends ElasticsearchRepository<DocumenExterne, Long> {
}
