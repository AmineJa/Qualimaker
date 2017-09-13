(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EtatDemandeDetailController', EtatDemandeDetailController);

    EtatDemandeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EtatDemande'];

    function EtatDemandeDetailController($scope, $rootScope, $stateParams, previousState, entity, EtatDemande) {
        var vm = this;

        vm.etatDemande = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:etatDemandeUpdate', function(event, result) {
            vm.etatDemande = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
