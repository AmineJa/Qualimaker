(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('ProgrammeSearch', ProgrammeSearch);

    ProgrammeSearch.$inject = ['$resource'];

    function ProgrammeSearch($resource) {
        var resourceUrl =  'api/_search/programmes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
