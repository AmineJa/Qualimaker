(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('NatureAbs', NatureAbs);

    NatureAbs.$inject = ['$resource'];

    function NatureAbs ($resource) {
        var resourceUrl =  'api/nature-abs/:id';

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
