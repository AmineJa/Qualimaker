package org.qualimaker.repository.search;

import org.qualimaker.domain.Naturediscipline;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Naturediscipline entity.
 */
public interface NaturedisciplineSearchRepository extends ElasticsearchRepository<Naturediscipline, Long> {
}
