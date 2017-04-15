(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Formation', Formation);

    Formation.$inject = ['$resource', 'DateUtils'];

    function Formation ($resource, DateUtils) {
        var resourceUrl =  'api/formations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateD = DateUtils.convertDateTimeFromServer(data.dateD);
                        data.dateF = DateUtils.convertDateTimeFromServer(data.dateF);
                        data.daterec = DateUtils.convertDateTimeFromServer(data.daterec);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
