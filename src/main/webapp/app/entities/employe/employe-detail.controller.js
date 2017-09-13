(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('EmployeDetailController', EmployeDetailController);

    EmployeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Employe', 'Conge', 'Sites', 'Groupe', 'Serviice', 'Formation', 'DocumenExterne', 'Remplacer', 'DroitaccesDocument', 'Carriere', 'Profilsfonction', 'Competences'];

    function EmployeDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Employe, Conge, Sites, Groupe, Serviice, Formation, DocumenExterne, Remplacer, DroitaccesDocument, Carriere, Profilsfonction, Competences) {
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
