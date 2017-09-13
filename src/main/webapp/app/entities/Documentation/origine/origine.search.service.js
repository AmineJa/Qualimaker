(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('OrigineSearch', OrigineSearch);

    OrigineSearch.$inject = ['$resource'];

    function OrigineSearch($resource) {
        var resourceUrl =  'api/_search/origines/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
