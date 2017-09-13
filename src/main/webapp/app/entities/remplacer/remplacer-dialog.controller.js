(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('RemplacerDialogController', RemplacerDialogController);

    RemplacerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Remplacer', 'Employe', 'Profilsfonction'];

    function RemplacerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Remplacer, Employe, Profilsfonction) {
        var vm = this;

        vm.remplacer = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.employes = Employe.query();
        vm.profilsfonctions = Profilsfonction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.remplacer.id !== null) {
                Remplacer.update(vm.remplacer, onSaveSuccess, onSaveError);
            } else {
                Remplacer.save(vm.remplacer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:remplacerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.debut = false;
        vm.datePickerOpenStatus.fin = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
