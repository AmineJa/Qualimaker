(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('DomaineCompetenceSearch', DomaineCompetenceSearch);

    DomaineCompetenceSearch.$inject = ['$resource'];

    function DomaineCompetenceSearch($resource) {
        var resourceUrl =  'api/_search/domaine-competences/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
