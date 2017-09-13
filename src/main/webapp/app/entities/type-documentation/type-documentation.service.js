(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('TypeDocumentation', TypeDocumentation);

    TypeDocumentation.$inject = ['$resource'];

    function TypeDocumentation ($resource) {
        var resourceUrl =  'api/type-documentations/:id';

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
