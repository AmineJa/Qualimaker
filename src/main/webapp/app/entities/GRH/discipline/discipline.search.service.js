(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('DisciplineSearch', DisciplineSearch);

    DisciplineSearch.$inject = ['$resource'];

    function DisciplineSearch($resource) {
        var resourceUrl =  'api/_search/disciplines/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
