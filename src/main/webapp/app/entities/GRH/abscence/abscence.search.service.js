(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('AbscenceSearch', AbscenceSearch);

    AbscenceSearch.$inject = ['$resource'];

    function AbscenceSearch($resource) {
        var resourceUrl =  'api/_search/abscences/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
