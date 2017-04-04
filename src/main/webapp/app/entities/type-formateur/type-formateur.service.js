(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('TypeFormateur', TypeFormateur);

    TypeFormateur.$inject = ['$resource'];

    function TypeFormateur ($resource) {
        var resourceUrl =  'api/type-formateurs/:id';

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
