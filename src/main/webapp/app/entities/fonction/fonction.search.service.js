(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('FonctionSearch', FonctionSearch);

    FonctionSearch.$inject = ['$resource'];

    function FonctionSearch($resource) {
        var resourceUrl =  'api/_search/fonctions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
