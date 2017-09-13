(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('ProcessusSearch', ProcessusSearch);

    ProcessusSearch.$inject = ['$resource'];

    function ProcessusSearch($resource) {
        var resourceUrl =  'api/_search/processuses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
