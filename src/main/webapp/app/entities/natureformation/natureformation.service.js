(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Natureformation', Natureformation);

    Natureformation.$inject = ['$resource'];

    function Natureformation ($resource) {
        var resourceUrl =  'api/natureformations/:id';

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
