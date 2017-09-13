(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('LieuxClassementSearch', LieuxClassementSearch);

    LieuxClassementSearch.$inject = ['$resource'];

    function LieuxClassementSearch($resource) {
        var resourceUrl =  'api/_search/lieux-classements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
