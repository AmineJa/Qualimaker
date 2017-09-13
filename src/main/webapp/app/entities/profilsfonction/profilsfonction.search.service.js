(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('ProfilsfonctionSearch', ProfilsfonctionSearch);

    ProfilsfonctionSearch.$inject = ['$resource'];

    function ProfilsfonctionSearch($resource) {
        var resourceUrl =  'api/_search/profilsfonctions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
