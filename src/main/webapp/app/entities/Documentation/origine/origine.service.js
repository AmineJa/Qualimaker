(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Origine', Origine);

    Origine.$inject = ['$resource'];

    function Origine ($resource) {
        var resourceUrl =  'api/origines/:id';

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
