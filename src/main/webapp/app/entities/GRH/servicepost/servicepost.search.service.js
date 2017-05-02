(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('ServicepostSearch', ServicepostSearch);

    ServicepostSearch.$inject = ['$resource'];

    function ServicepostSearch($resource) {
        var resourceUrl =  'api/_search/serviceposts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
