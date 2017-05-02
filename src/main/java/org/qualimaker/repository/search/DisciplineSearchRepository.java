package org.qualimaker.repository.search;

import org.qualimaker.domain.Discipline;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Discipline entity.
 */
public interface DisciplineSearchRepository extends ElasticsearchRepository<Discipline, Long> {
}
