(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('AbscenceDialogController', AbscenceDialogController);

    AbscenceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Abscence'];

    function AbscenceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Abscence) {
        var vm = this;

        vm.abscence = entity;
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
            if (vm.abscence.id !== null) {
                Abscence.update(vm.abscence, onSaveSuccess, onSaveError);
            } else {
                Abscence.save(vm.abscence, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:abscenceUpdate', result);
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
