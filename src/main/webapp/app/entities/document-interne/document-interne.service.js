(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('DocumentInterne', DocumentInterne);

    DocumentInterne.$inject = ['$resource', 'DateUtils'];

    function DocumentInterne ($resource, DateUtils) {
        var resourceUrl =  'api/document-internes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
