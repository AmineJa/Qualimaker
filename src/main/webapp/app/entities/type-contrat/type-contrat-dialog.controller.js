(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('TypeContratDialogController', TypeContratDialogController);

    TypeContratDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeContrat', 'Carriere'];

    function TypeContratDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeContrat, Carriere) {
        var vm = this;

        vm.typeContrat = entity;
        vm.clear = clear;
        vm.save = save;
        vm.carrieres = Carriere.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.typeContrat.id !== null) {
                TypeContrat.update(vm.typeContrat, onSaveSuccess, onSaveError);
            } else {
                TypeContrat.save(vm.typeContrat, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:typeContratUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
