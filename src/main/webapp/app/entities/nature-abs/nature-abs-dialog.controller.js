(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('NatureAbsDialogController', NatureAbsDialogController);

    NatureAbsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NatureAbs'];

    function NatureAbsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NatureAbs) {
        var vm = this;

        vm.natureAbs = entity;
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
            if (vm.natureAbs.id !== null) {
                NatureAbs.update(vm.natureAbs, onSaveSuccess, onSaveError);
            } else {
                NatureAbs.save(vm.natureAbs, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:natureAbsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
