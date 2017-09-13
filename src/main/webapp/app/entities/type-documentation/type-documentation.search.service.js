(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('TypeDocumentationSearch', TypeDocumentationSearch);

    TypeDocumentationSearch.$inject = ['$resource'];

    function TypeDocumentationSearch($resource) {
        var resourceUrl =  'api/_search/type-documentations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
