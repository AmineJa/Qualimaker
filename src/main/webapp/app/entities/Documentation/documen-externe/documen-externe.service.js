(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('DocumenExterne', DocumenExterne);

    DocumenExterne.$inject = ['$resource', 'DateUtils'];

    function DocumenExterne ($resource, DateUtils) {
        var resourceUrl =  'api/documen-externes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.daterevision = DateUtils.convertDateTimeFromServer(data.daterevision);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
