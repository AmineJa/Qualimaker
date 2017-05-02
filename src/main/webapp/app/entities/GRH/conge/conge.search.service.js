(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('CongeSearch', CongeSearch);

    CongeSearch.$inject = ['$resource'];

    function CongeSearch($resource) {
        var resourceUrl =  'api/_search/conges/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
