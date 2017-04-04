(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('DomaineCompetence', DomaineCompetence);

    DomaineCompetence.$inject = ['$resource'];

    function DomaineCompetence ($resource) {
        var resourceUrl =  'api/domaine-competences/:id';

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
