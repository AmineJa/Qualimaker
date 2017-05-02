(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('CalendrierDialogController', CalendrierDialogController);

    CalendrierDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Calendrier', 'Jour'];

    function CalendrierDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Calendrier, Jour) {
        var vm = this;

        vm.calendrier = entity;
        vm.clear = clear;
        vm.save = save;
        vm.jours = Jour.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.calendrier.id !== null) {
                Calendrier.update(vm.calendrier, onSaveSuccess, onSaveError);
            } else {
                Calendrier.save(vm.calendrier, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:calendrierUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
