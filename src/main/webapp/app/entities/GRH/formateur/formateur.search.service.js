(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('FormateurSearch', FormateurSearch);

    FormateurSearch.$inject = ['$resource'];

    function FormateurSearch($resource) {
        var resourceUrl =  'api/_search/formateurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
