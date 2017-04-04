(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Etat', Etat);

    Etat.$inject = ['$resource'];

    function Etat ($resource) {
        var resourceUrl =  'api/etats/:id';

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
