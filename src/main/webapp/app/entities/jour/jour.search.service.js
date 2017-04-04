(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('JourSearch', JourSearch);

    JourSearch.$inject = ['$resource'];

    function JourSearch($resource) {
        var resourceUrl =  'api/_search/jours/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
