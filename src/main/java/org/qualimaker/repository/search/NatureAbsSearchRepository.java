package org.qualimaker.repository.search;

import org.qualimaker.domain.NatureAbs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the NatureAbs entity.
 */
public interface NatureAbsSearchRepository extends ElasticsearchRepository<NatureAbs, Long> {
}
