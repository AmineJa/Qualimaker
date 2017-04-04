(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('EtatSearch', EtatSearch);

    EtatSearch.$inject = ['$resource'];

    function EtatSearch($resource) {
        var resourceUrl =  'api/_search/etats/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
