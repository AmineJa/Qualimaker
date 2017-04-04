(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Evaluation', Evaluation);

    Evaluation.$inject = ['$resource', 'DateUtils'];

    function Evaluation ($resource, DateUtils) {
        var resourceUrl =  'api/evaluations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.anne = DateUtils.convertDateTimeFromServer(data.anne);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
