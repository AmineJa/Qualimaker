(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('ProgrammeDialogController', ProgrammeDialogController);

    ProgrammeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Programme'];

    function ProgrammeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Programme) {
        var vm = this;

        vm.programme = entity;
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
            if (vm.programme.id !== null) {
                Programme.update(vm.programme, onSaveSuccess, onSaveError);
            } else {
                Programme.save(vm.programme, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:programmeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
