(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('LieuxClassement', LieuxClassement);

    LieuxClassement.$inject = ['$resource'];

    function LieuxClassement ($resource) {
        var resourceUrl =  'api/lieux-classements/:id';

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
