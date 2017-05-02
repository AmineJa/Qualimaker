(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FormationDetailController', FormationDetailController);

    FormationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Formation', 'DemandeFormation', 'FormationComp', 'Formateur', 'Natureformation', 'Fichierjoint', 'Critereevaluation', 'Jour', 'Events', 'Employe'];

    function FormationDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Formation, DemandeFormation, FormationComp, Formateur, Natureformation, Fichierjoint, Critereevaluation, Jour, Events, Employe) {
        var vm = this;

        vm.formation = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('qualiMakerApp:formationUpdate', function(event, result) {
            vm.formation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
