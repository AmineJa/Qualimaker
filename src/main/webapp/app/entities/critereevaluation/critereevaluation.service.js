(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Critereevaluation', Critereevaluation);

    Critereevaluation.$inject = ['$resource'];

    function Critereevaluation ($resource) {
        var resourceUrl =  'api/critereevaluations/:id';

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
