(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('NatureAbsSearch', NatureAbsSearch);

    NatureAbsSearch.$inject = ['$resource'];

    function NatureAbsSearch($resource) {
        var resourceUrl =  'api/_search/nature-abs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
