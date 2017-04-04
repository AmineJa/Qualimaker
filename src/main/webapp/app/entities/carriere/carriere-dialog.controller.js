(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CarriereDialogController', CarriereDialogController);

    CarriereDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Carriere', 'TypeContrat'];

    function CarriereDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Carriere, TypeContrat) {
        var vm = this;

        vm.carriere = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.typecontrats = TypeContrat.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.carriere.id !== null) {
                Carriere.update(vm.carriere, onSaveSuccess, onSaveError);
            } else {
                Carriere.save(vm.carriere, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:carriereUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.debutINt = false;
        vm.datePickerOpenStatus.finINT = false;
        vm.datePickerOpenStatus.dateRec = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
