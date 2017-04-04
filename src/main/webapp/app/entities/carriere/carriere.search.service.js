(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('CarriereSearch', CarriereSearch);

    CarriereSearch.$inject = ['$resource'];

    function CarriereSearch($resource) {
        var resourceUrl =  'api/_search/carrieres/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
