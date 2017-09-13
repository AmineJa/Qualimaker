(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .factory('EnregistrementSearch', EnregistrementSearch);

    EnregistrementSearch.$inject = ['$resource'];

    function EnregistrementSearch($resource) {
        var resourceUrl =  'api/_search/enregistrements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
