(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('EtatDemandeSearch', EtatDemandeSearch);

    EtatDemandeSearch.$inject = ['$resource'];

    function EtatDemandeSearch($resource) {
        var resourceUrl =  'api/_search/etat-demandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
