(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('CompetencesSearch', CompetencesSearch);

    CompetencesSearch.$inject = ['$resource'];

    function CompetencesSearch($resource) {
        var resourceUrl =  'api/_search/competences/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
