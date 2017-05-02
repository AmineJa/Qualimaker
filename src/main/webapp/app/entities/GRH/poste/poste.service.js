(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Poste', Poste);

    Poste.$inject = ['$resource'];

    function Poste ($resource) {
        var resourceUrl =  'api/postes/:id';

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
