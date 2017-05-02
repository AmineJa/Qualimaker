(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Formateur', Formateur);

    Formateur.$inject = ['$resource'];

    function Formateur ($resource) {
        var resourceUrl =  'api/formateurs/:id';

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
