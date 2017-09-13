(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Profilsfonction', Profilsfonction);

    Profilsfonction.$inject = ['$resource'];

    function Profilsfonction ($resource) {
        var resourceUrl =  'api/profilsfonctions/:id';

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
