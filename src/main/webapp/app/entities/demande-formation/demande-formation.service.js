(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('DemandeFormation', DemandeFormation);

    DemandeFormation.$inject = ['$resource', 'DateUtils'];

    function DemandeFormation ($resource, DateUtils) {
        var resourceUrl =  'api/demande-formations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateDemande = DateUtils.convertDateTimeFromServer(data.dateDemande);
                        data.datesouhaite = DateUtils.convertDateTimeFromServer(data.datesouhaite);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
