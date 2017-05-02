(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Discipline', Discipline);

    Discipline.$inject = ['$resource', 'DateUtils'];

    function Discipline ($resource, DateUtils) {
        var resourceUrl =  'api/disciplines/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateD = DateUtils.convertDateTimeFromServer(data.dateD);
                        data.dateF = DateUtils.convertDateTimeFromServer(data.dateF);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
