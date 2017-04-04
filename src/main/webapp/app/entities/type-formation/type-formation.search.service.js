(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('TypeFormationSearch', TypeFormationSearch);

    TypeFormationSearch.$inject = ['$resource'];

    function TypeFormationSearch($resource) {
        var resourceUrl =  'api/_search/type-formations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
