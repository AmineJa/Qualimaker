package org.qualimaker.repository.search;

import org.qualimaker.domain.Competences;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Competences entity.
 */
public interface CompetencesSearchRepository extends ElasticsearchRepository<Competences, Long> {
}
