(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('NaturedisciplineSearch', NaturedisciplineSearch);

    NaturedisciplineSearch.$inject = ['$resource'];

    function NaturedisciplineSearch($resource) {
        var resourceUrl =  'api/_search/naturedisciplines/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
