package org.qualimaker.repository.search;

import org.qualimaker.domain.DroitaccesDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DroitaccesDocument entity.
 */
public interface DroitaccesDocumentSearchRepository extends ElasticsearchRepository<DroitaccesDocument, Long> {
}
