(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ProcessusDetailController', ProcessusDetailController);

    ProcessusDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Processus', 'DocumentInterne'];

    function ProcessusDetailController($scope, $rootScope, $stateParams, previousState, entity, Processus, DocumentInterne) {
        var vm = this;

        vm.processus = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:processusUpdate', function(event, result) {
            vm.processus = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
