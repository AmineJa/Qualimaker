(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('EvaluationFormationSearch', EvaluationFormationSearch);

    EvaluationFormationSearch.$inject = ['$resource'];

    function EvaluationFormationSearch($resource) {
        var resourceUrl =  'api/_search/evaluation-formations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
