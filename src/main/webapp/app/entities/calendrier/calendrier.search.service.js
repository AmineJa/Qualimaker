(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('CalendrierSearch', CalendrierSearch);

    CalendrierSearch.$inject = ['$resource'];

    function CalendrierSearch($resource) {
        var resourceUrl =  'api/_search/calendriers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
