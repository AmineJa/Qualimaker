(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CongeDialogController', CongeDialogController);

    CongeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Conge', 'Employe', 'TypeConge'];

    function CongeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Conge, Employe, TypeConge) {
        var vm = this;

        vm.conge = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.employes = Employe.query();
        vm.typeconges = TypeConge.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.conge.id !== null) {
                Conge.update(vm.conge, onSaveSuccess, onSaveError);
            } else {
                Conge.save(vm.conge, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:congeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateD = false;
        vm.datePickerOpenStatus.dateF = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
