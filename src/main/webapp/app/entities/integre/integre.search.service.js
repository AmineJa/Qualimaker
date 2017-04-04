(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('IntegreSearch', IntegreSearch);

    IntegreSearch.$inject = ['$resource'];

    function IntegreSearch($resource) {
        var resourceUrl =  'api/_search/integres/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
