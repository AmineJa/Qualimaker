(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Servicepost', Servicepost);

    Servicepost.$inject = ['$resource'];

    function Servicepost ($resource) {
        var resourceUrl =  'api/serviceposts/:id';

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
