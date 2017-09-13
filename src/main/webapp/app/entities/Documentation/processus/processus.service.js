(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Processus', Processus);

    Processus.$inject = ['$resource'];

    function Processus ($resource) {
        var resourceUrl =  'api/processuses/:id';

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
