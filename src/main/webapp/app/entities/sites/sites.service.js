(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Sites', Sites);

    Sites.$inject = ['$resource'];

    function Sites ($resource) {
        var resourceUrl =  'api/sites/:id';

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
