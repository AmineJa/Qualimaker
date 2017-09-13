(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('DroitaccesDocumentSearch', DroitaccesDocumentSearch);

    DroitaccesDocumentSearch.$inject = ['$resource'];

    function DroitaccesDocumentSearch($resource) {
        var resourceUrl =  'api/_search/droitacces-documents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
