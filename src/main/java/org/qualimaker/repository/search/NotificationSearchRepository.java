package org.qualimaker.repository.search;

import org.qualimaker.domain.Notification;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Notification entity.
 */
public interface NotificationSearchRepository extends ElasticsearchRepository<Notification, Long> {
}
