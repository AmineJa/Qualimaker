(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('NatureformationSearch', NatureformationSearch);

    NatureformationSearch.$inject = ['$resource'];

    function NatureformationSearch($resource) {
        var resourceUrl =  'api/_search/natureformations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
