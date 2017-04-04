package org.qualimaker.repository.search;

import org.qualimaker.domain.FormationComp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FormationComp entity.
 */
public interface FormationCompSearchRepository extends ElasticsearchRepository<FormationComp, Long> {
}
