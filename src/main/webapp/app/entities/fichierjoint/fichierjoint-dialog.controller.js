(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('FichierjointDialogController', FichierjointDialogController);

    FichierjointDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fichierjoint'];

    function FichierjointDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Fichierjoint) {
        var vm = this;

        vm.fichierjoint = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.fichierjoint.id !== null) {
                Fichierjoint.update(vm.fichierjoint, onSaveSuccess, onSaveError);
            } else {
                Fichierjoint.save(vm.fichierjoint, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:fichierjointUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
