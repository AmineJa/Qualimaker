(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('NotificationSearch', NotificationSearch);

    NotificationSearch.$inject = ['$resource'];

    function NotificationSearch($resource) {
        var resourceUrl =  'api/_search/notifications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
