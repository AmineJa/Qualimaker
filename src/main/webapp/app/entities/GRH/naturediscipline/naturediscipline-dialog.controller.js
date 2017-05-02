(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('NaturedisciplineDialogController', NaturedisciplineDialogController);

    NaturedisciplineDialogController.$inject = ['$timeout', '$scope','$state', '$stateParams', '$uibModalInstance', 'entity', 'Naturediscipline'];

    function NaturedisciplineDialogController ($timeout, $scope,$state, $stateParams, $uibModalInstance, entity, Naturediscipline) {
        var vm = this;

        vm.naturediscipline = entity;
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
            if (vm.naturediscipline.id !== null) {
                Naturediscipline.update(vm.naturediscipline, onSaveSuccess, onSaveError);
            } else {
               /* $state.loadAll();*/

                Naturediscipline.save(vm.naturediscipline, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:naturedisciplineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
