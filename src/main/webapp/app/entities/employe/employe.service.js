(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Employe', Employe);

    Employe.$inject = ['$resource', 'DateUtils'];

    function Employe ($resource, DateUtils) {
        var resourceUrl =  'api/employes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateN = DateUtils.convertDateTimeFromServer(data.dateN);
                        data.delivrele = DateUtils.convertDateTimeFromServer(data.delivrele);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
