(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('IntegreDialogController', IntegreDialogController);

    IntegreDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Integre'];

    function IntegreDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Integre) {
        var vm = this;

        vm.integre = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.integre.id !== null) {
                Integre.update(vm.integre, onSaveSuccess, onSaveError);
            } else {
                Integre.save(vm.integre, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:integreUpdate', result);
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
