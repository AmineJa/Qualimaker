(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DroitaccesDocumentDetailController', DroitaccesDocumentDetailController);

    DroitaccesDocumentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DroitaccesDocument', 'Employe', 'DocumentInterne'];

    function DroitaccesDocumentDetailController($scope, $rootScope, $stateParams, previousState, entity, DroitaccesDocument, Employe, DocumentInterne) {
        var vm = this;

        vm.droitaccesDocument = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:droitaccesDocumentUpdate', function(event, result) {
            vm.droitaccesDocument = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
