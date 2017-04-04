(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Jour', Jour);

    Jour.$inject = ['$resource', 'DateUtils'];

    function Jour ($resource, DateUtils) {
        var resourceUrl =  'api/jours/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.jour = DateUtils.convertDateTimeFromServer(data.jour);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
