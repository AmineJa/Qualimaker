(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('DocumenExterneSearch', DocumenExterneSearch);

    DocumenExterneSearch.$inject = ['$resource'];

    function DocumenExterneSearch($resource) {
        var resourceUrl =  'api/_search/documen-externes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
