(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Remplacer', Remplacer);

    Remplacer.$inject = ['$resource', 'DateUtils'];

    function Remplacer ($resource, DateUtils) {
        var resourceUrl =  'api/remplacers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.debut = DateUtils.convertDateTimeFromServer(data.debut);
                        data.fin = DateUtils.convertDateTimeFromServer(data.fin);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
