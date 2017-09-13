(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('EtatDemande', EtatDemande);

    EtatDemande.$inject = ['$resource'];

    function EtatDemande ($resource) {
        var resourceUrl =  'api/etat-demandes/:id';

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
