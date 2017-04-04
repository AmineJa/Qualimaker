(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FormationDetailController', FormationDetailController);

    FormationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Formation', 'DemandeFormation', 'FormationComp', 'Formateur', 'Natureformation', 'Fichierjoint', 'Critereevaluation', 'Jour', 'Employe'];

    function FormationDetailController($scope, $rootScope, $stateParams, previousState, entity, Formation, DemandeFormation, FormationComp, Formateur, Natureformation, Fichierjoint, Critereevaluation, Jour, Employe) {
        var vm = this;

        vm.formation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('qualiMakerApp:formationUpdate', function(event, result) {
            vm.formation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
