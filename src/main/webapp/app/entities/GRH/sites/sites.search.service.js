(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('SitesSearch', SitesSearch);

    SitesSearch.$inject = ['$resource'];

    function SitesSearch($resource) {
        var resourceUrl =  'api/_search/sites/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
