(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('RemplacerSearch', RemplacerSearch);

    RemplacerSearch.$inject = ['$resource'];

    function RemplacerSearch($resource) {
        var resourceUrl =  'api/_search/remplacers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
