(function() {
    'use strict';
    angular
        .module('qualiMakerApp')
        .factory('Carriere', Carriere);

    Carriere.$inject = ['$resource', 'DateUtils'];

    function Carriere ($resource, DateUtils) {
        var resourceUrl =  'api/carrieres/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.debutINt = DateUtils.convertDateTimeFromServer(data.debutINt);
                        data.finINT = DateUtils.convertDateTimeFromServer(data.finINT);
                        data.dateRec = DateUtils.convertDateTimeFromServer(data.dateRec);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
