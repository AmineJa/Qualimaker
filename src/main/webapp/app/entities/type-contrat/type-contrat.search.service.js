(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('TypeContratSearch', TypeContratSearch);

    TypeContratSearch.$inject = ['$resource'];

    function TypeContratSearch($resource) {
        var resourceUrl =  'api/_search/type-contrats/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
