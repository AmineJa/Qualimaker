(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('EvaluationFormation', EvaluationFormation);

    EvaluationFormation.$inject = ['$resource'];

    function EvaluationFormation ($resource) {
        var resourceUrl =  'api/evaluation-formations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
