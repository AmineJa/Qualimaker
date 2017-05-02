(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('PosteSearch', PosteSearch);

    PosteSearch.$inject = ['$resource'];

    function PosteSearch($resource) {
        var resourceUrl =  'api/_search/postes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
