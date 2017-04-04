(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Fichierjoint', Fichierjoint);

    Fichierjoint.$inject = ['$resource'];

    function Fichierjoint ($resource) {
        var resourceUrl =  'api/fichierjoints/:id';

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
