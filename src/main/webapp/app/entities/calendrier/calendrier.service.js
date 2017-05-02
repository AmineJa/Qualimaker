(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Calendrier', Calendrier);

    Calendrier.$inject = ['$resource'];

    function Calendrier ($resource) {
        var resourceUrl =  'api/calendriers/:id';

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
