package org.qualimaker.repository.search;

import org.qualimaker.domain.TypeDocumentation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TypeDocumentation entity.
 */
public interface TypeDocumentationSearchRepository extends ElasticsearchRepository<TypeDocumentation, Long> {
}
