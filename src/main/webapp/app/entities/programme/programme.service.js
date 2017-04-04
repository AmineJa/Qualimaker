(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Programme', Programme);

    Programme.$inject = ['$resource'];

    function Programme ($resource) {
        var resourceUrl =  'api/programmes/:id';

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
