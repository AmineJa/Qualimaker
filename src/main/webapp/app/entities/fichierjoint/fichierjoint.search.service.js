(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('FichierjointSearch', FichierjointSearch);

    FichierjointSearch.$inject = ['$resource'];

    function FichierjointSearch($resource) {
        var resourceUrl =  'api/_search/fichierjoints/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
