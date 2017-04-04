(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('DemandeFormationDialogController', DemandeFormationDialogController);

    DemandeFormationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DemandeFormation', 'Employe'];

    function DemandeFormationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DemandeFormation, Employe) {
        var vm = this;

        vm.demandeFormation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.employes = Employe.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.demandeFormation.id !== null) {
                DemandeFormation.update(vm.demandeFormation, onSaveSuccess, onSaveError);
            } else {
                DemandeFormation.save(vm.demandeFormation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:demandeFormationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateDemande = false;
        vm.datePickerOpenStatus.datesouhaite = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
