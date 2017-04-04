(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DemandeFormationDetailController', DemandeFormationDetailController);

    DemandeFormationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DemandeFormation', 'Employe'];

    function DemandeFormationDetailController($scope, $rootScope, $stateParams, previousState, entity, DemandeFormation, Employe) {
        var vm = this;

        vm.demandeFormation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:demandeFormationUpdate', function(event, result) {
            vm.demandeFormation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
