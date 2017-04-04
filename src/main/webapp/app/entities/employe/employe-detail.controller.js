(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EmployeDetailController', EmployeDetailController);

    EmployeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Employe', 'Carriere', 'Conge', 'Sites', 'Groupe', 'Abscence', 'Serviice', 'Formation', 'Fonction'];

    function EmployeDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Employe, Carriere, Conge, Sites, Groupe, Abscence, Serviice, Formation, Fonction) {
        var vm = this;

        vm.employe = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('qualiMakerApp:employeUpdate', function(event, result) {
            vm.employe = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
