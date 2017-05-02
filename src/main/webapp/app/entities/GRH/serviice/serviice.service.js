(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Serviice', Serviice);

    Serviice.$inject = ['$resource'];

    function Serviice ($resource) {
        var resourceUrl =  'api/serviices/:id';

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
