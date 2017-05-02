(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('TypeConge', TypeConge);

    TypeConge.$inject = ['$resource'];

    function TypeConge ($resource) {
        var resourceUrl =  'api/type-conges/:id';

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
