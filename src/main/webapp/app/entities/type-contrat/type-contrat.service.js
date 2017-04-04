(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('TypeContrat', TypeContrat);

    TypeContrat.$inject = ['$resource'];

    function TypeContrat ($resource) {
        var resourceUrl =  'api/type-contrats/:id';

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
