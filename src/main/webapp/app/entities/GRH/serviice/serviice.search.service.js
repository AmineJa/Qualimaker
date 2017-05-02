(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('ServiiceSearch', ServiiceSearch);

    ServiiceSearch.$inject = ['$resource'];

    function ServiiceSearch($resource) {
        var resourceUrl =  'api/_search/serviices/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
