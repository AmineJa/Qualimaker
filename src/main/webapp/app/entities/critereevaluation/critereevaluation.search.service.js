(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('CritereevaluationSearch', CritereevaluationSearch);

    CritereevaluationSearch.$inject = ['$resource'];

    function CritereevaluationSearch($resource) {
        var resourceUrl =  'api/_search/critereevaluations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
