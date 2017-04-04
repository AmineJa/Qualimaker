(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('FormationComp', FormationComp);

    FormationComp.$inject = ['$resource'];

    function FormationComp ($resource) {
        var resourceUrl =  'api/formation-comps/:id';

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
