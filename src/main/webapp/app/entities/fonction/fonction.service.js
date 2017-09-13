(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Fonction', Fonction);

    Fonction.$inject = ['$resource'];

    function Fonction ($resource) {
        var resourceUrl =  'api/fonctions/:id';

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
