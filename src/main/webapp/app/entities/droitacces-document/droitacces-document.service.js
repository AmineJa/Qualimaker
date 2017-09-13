(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('DroitaccesDocument', DroitaccesDocument);

    DroitaccesDocument.$inject = ['$resource'];

    function DroitaccesDocument ($resource) {
        var resourceUrl =  'api/droitacces-documents/:id';

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
