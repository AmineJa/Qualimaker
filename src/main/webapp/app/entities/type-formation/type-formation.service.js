(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('TypeFormation', TypeFormation);

    TypeFormation.$inject = ['$resource'];

    function TypeFormation ($resource) {
        var resourceUrl =  'api/type-formations/:id';

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
