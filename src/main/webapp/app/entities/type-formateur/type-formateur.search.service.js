(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('TypeFormateurSearch', TypeFormateurSearch);

    TypeFormateurSearch.$inject = ['$resource'];

    function TypeFormateurSearch($resource) {
        var resourceUrl =  'api/_search/type-formateurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
