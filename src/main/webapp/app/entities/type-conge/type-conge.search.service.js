(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('TypeCongeSearch', TypeCongeSearch);

    TypeCongeSearch.$inject = ['$resource'];

    function TypeCongeSearch($resource) {
        var resourceUrl =  'api/_search/type-conges/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
