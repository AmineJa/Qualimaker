(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('FormationCompSearch', FormationCompSearch);

    FormationCompSearch.$inject = ['$resource'];

    function FormationCompSearch($resource) {
        var resourceUrl =  'api/_search/formation-comps/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
