(function() {
    'use strict';

    angular
        .module('qualiMakerApp')
        .controller('OrigineDialogController', OrigineDialogController);

    OrigineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Origine'];

    function OrigineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Origine) {
        var vm = this;

        vm.origine = entity;
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
            if (vm.origine.id !== null) {
                Origine.update(vm.origine, onSaveSuccess, onSaveError);
            } else {
                Origine.save(vm.origine, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('qualiMakerApp:origineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
