(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('DocumentInterneSearch', DocumentInterneSearch);

    DocumentInterneSearch.$inject = ['$resource'];

    function DocumentInterneSearch($resource) {
        var resourceUrl =  'api/_search/document-internes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
