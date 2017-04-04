(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('DemandeFormationSearch', DemandeFormationSearch);

    DemandeFormationSearch.$inject = ['$resource'];

    function DemandeFormationSearch($resource) {
        var resourceUrl =  'api/_search/demande-formations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
