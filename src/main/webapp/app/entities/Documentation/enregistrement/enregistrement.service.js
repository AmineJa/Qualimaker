(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Enregistrement', Enregistrement);

    Enregistrement.$inject = ['$resource', 'DateUtils'];

    function Enregistrement ($resource, DateUtils) {
        var resourceUrl =  'api/enregistrements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
