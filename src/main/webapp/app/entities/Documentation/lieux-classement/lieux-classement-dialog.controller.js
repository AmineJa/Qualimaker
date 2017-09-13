(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('LieuxClassementDialogController', LieuxClassementDialogController);

    LieuxClassementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LieuxClassement'];

    function LieuxClassementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LieuxClassement) {
        var vm = this;

        vm.lieuxClassement = entity;
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
            if (vm.lieuxClassement.id !== null) {
                LieuxClassement.update(vm.lieuxClassement, onSaveSuccess, onSaveError);
            } else {
                LieuxClassement.save(vm.lieuxClassement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:lieuxClassementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
